package com.example.vision

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vision.data.Product
import com.example.vision.data.ProductRepository
import com.example.vision.data.Store
import com.example.vision.ui.ProductAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var progress: ProgressBar
    private lateinit var empty: TextView
    private lateinit var search: SearchView
    private lateinit var title: TextView

    private var allProducts = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.productRecycler)
        progress = findViewById(R.id.progress)
        empty = findViewById(R.id.emptyText)
        search = findViewById(R.id.searchView)
        title = findViewById(R.id.screenTitle)

        adapter = ProductAdapter(emptyList(), ::showProduct)

        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter

        setupSearch()
        setupNavigation()
        loadProducts()
    }

    // Load REST API products and combine them with
    // VISION's own sportswear catalogue.
    private fun loadProducts() {
        progress.visibility = View.VISIBLE

        lifecycleScope.launch {

            val apiProducts = try {
                ProductRepository.loadProducts()
            } catch (_: Exception) {
                emptyList()
            }

            val visionProducts = ProductRepository.fallbackProducts()

            allProducts = apiProducts + visionProducts

            Store.products = allProducts

            progress.visibility = View.GONE

            showHome()
        }
    }

    private fun setupSearch() {
        search.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(q: String?) = false

                override fun onQueryTextChange(q: String?): Boolean {

                    val text = q.orEmpty()

                    val filteredProducts = allProducts.filter {
                        it.title.contains(text, ignoreCase = true) ||
                                it.category.contains(text, ignoreCase = true)
                    }

                    adapter.submit(filteredProducts)

                    empty.text = "No products found."
                    empty.visibility =
                        if (filteredProducts.isEmpty()) View.VISIBLE
                        else View.GONE

                    return true
                }
            }
        )
    }

    private fun setupNavigation() {

        findViewById<BottomNavigationView>(R.id.bottomNav)
            .setOnItemSelectedListener {

                when (it.itemId) {

                    R.id.nav_home -> showHome()

                    R.id.nav_favourites -> showFavourites()

                    R.id.nav_cart -> showCart()

                    R.id.nav_profile -> showProfile()
                }

                true
            }
    }

    // Home catalogue with Athlete Profile recommendations.
    private fun showHome() {

        search.visibility = View.VISIBLE
        recycler.visibility = View.VISIBLE
        empty.visibility = View.GONE

        val selectedSport = getSharedPreferences(
            "vision",
            MODE_PRIVATE
        ).getString("sport", "Football") ?: "Football"

        title.text = "DISCOVER VISION • $selectedSport"

        /*
         * The Athlete Profile does not remove the rest of the catalogue.
         * Instead, products related to the athlete's selected sport are
         * placed first as recommendations.
         */
        val recommended = allProducts.filter {
            matchesSport(it, selectedSport)
        }

        val otherProducts = allProducts.filterNot {
            matchesSport(it, selectedSport)
        }

        adapter.submit(recommended + otherProducts)
    }

    // Match catalogue categories to the Athlete Profile options.
    private fun matchesSport(
        product: Product,
        sport: String
    ): Boolean {

        val category = product.category.lowercase()
        val name = product.title.lowercase()

        return when (sport) {

            "Football" ->
                category.contains("football") ||
                        name.contains("football") ||
                        name.contains("match")

            "Basketball" ->
                category.contains("basketball") ||
                        name.contains("basketball") ||
                        name.contains("court")

            "Running" ->
                category.contains("running") ||
                        name.contains("running") ||
                        name.contains("run")

            "Gym & Training" ->
                category.contains("training") ||
                        category.contains("gym") ||
                        name.contains("training") ||
                        name.contains("performance")

            "Casual Sportswear" ->
                category.contains("casual") ||
                        category.contains("sportswear") ||
                        name.contains("hoodie") ||
                        name.contains("tracksuit")

            else -> false
        }
    }

    private fun showFavourites() {

        title.text = "YOUR FAVOURITES"

        search.visibility = View.GONE
        recycler.visibility = View.VISIBLE

        val favs = allProducts.filter {
            Store.favourites.contains(it.id)
        }

        adapter.submit(favs)

        empty.text =
            "No favourites yet. Tap the star on a product you love."

        empty.visibility =
            if (favs.isEmpty()) View.VISIBLE
            else View.GONE
    }

    private fun showCart() {

        val cartItems = allProducts.filter {
            Store.cart.containsKey(it.id)
        }

        val lines = cartItems.joinToString("\n\n") {

            val quantity = Store.cart[it.id] ?: 1

            "${it.title} ×$quantity\nR %.2f".format(
                it.price * 18 * quantity
            )
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(
                "Your Cart (${Store.cartCount()} items)"
            )
            .setMessage(
                if (lines.isBlank()) {

                    "Your cart is empty."

                } else {

                    "$lines\n\nTOTAL: R %.2f".format(
                        Store.cartTotal() * 18
                    )
                }
            )
            .setNegativeButton(
                "Continue shopping",
                null
            )
            .setPositiveButton(
                if (lines.isBlank()) "OK"
                else "Checkout"
            ) { _, _ ->

                if (lines.isNotBlank()) {

                    Toast.makeText(
                        this,
                        "Prototype checkout successful!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .show()
    }

    private fun showProfile() {

        val sports = arrayOf(
            "Football",
            "Basketball",
            "Running",
            "Gym & Training",
            "Casual Sportswear"
        )

        val preferences = getSharedPreferences(
            "vision",
            MODE_PRIVATE
        )

        val current =
            preferences.getString(
                "sport",
                "Football"
            ) ?: "Football"

        MaterialAlertDialogBuilder(this)
            .setTitle("Athlete Profile")
            .setSingleChoiceItems(
                sports,
                sports.indexOf(current)
            ) { dialog, which ->

                val selectedSport = sports[which]

                preferences.edit()
                    .putString(
                        "sport",
                        selectedSport
                    )
                    .apply()

                Toast.makeText(
                    this,
                    "Main sport saved: $selectedSport",
                    Toast.LENGTH_SHORT
                ).show()

                dialog.dismiss()

                /*
                 * Immediately refresh the catalogue so that
                 * recommendations for the selected sport appear first.
                 */
                showHome()
            }
            .setNegativeButton(
                "Close",
                null
            )
            .show()
    }

    private fun showProduct(p: Product) {

        val view = layoutInflater.inflate(
            R.layout.dialog_product,
            null
        )

        val image =
            view.findViewById<ImageView>(
                R.id.detailImage
            )

        if (p.thumbnail.isNotBlank()) {

            image.load(p.thumbnail) {

                placeholder(
                    R.drawable.product_placeholder
                )

                error(
                    R.drawable.product_placeholder
                )
            }

        } else {

            image.setImageResource(
                R.drawable.product_placeholder
            )
        }

        view.findViewById<TextView>(
            R.id.detailName
        ).text = p.title

        view.findViewById<TextView>(
            R.id.detailCategory
        ).text = p.category.uppercase()

        view.findViewById<TextView>(
            R.id.detailPrice
        ).text =
            "R %.2f".format(
                p.price * 18
            )

        view.findViewById<TextView>(
            R.id.detailDescription
        ).text = p.description

        MaterialAlertDialogBuilder(this)
            .setView(view)
            .setNegativeButton(
                "Close",
                null
            )
            .setPositiveButton(
                "Add to cart"
            ) { _, _ ->

                Store.addToCart(p.id)

                Toast.makeText(
                    this,
                    "Added to cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .show()
    }
}