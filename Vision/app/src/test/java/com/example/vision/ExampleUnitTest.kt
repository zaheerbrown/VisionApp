package com.example.vision

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun priceConversion_isCorrect() {
        val dollarPrice = 10.0
        val exchangeRate = 18.0

        val randPrice = dollarPrice * exchangeRate

        assertEquals(180.0, randPrice, 0.01)
    }

    @Test
    fun categoryFormatting_isCorrect() {
        val category = "sports"

        val formattedCategory = category.replaceFirstChar {
            it.uppercase()
        }

        assertEquals("Sports", formattedCategory)
    }

    @Test
    fun search_matchesProductTitle() {
        val productTitle = "VISION Performance Tee"
        val searchQuery = "performance"

        val matches = productTitle.contains(
            searchQuery,
            ignoreCase = true
        )

        assertTrue(matches)
    }

    @Test
    fun search_rejectsIncorrectProductTitle() {
        val productTitle = "VISION Elite Hoodie"
        val searchQuery = "shoes"

        val matches = productTitle.contains(
            searchQuery,
            ignoreCase = true
        )

        assertFalse(matches)
    }

    @Test
    fun cartTotal_isCalculatedCorrectly() {
        val productPrice = 250.0
        val quantity = 3

        val total = productPrice * quantity

        assertEquals(750.0, total, 0.01)
    }
}