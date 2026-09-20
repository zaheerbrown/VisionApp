# VISION – Athlete Sportswear Android App

VISION is a Kotlin Android prototype for athletes and sports enthusiasts who want modern clothing for training and everyday wear. It develops the concept researched in Part 1 into a functional shopping prototype.

## Features
- Product catalogue displayed in a RecyclerView grid
- RESTful API product retrieval with an offline VISION fallback catalogue
- Search by product name/category
- Product details including image, description, price and sizes
- Favourites/wishlist
- Shopping cart, quantities, total and prototype checkout
- Athlete Profile with saved main sport preference
- Bottom navigation and VISION-branded Material interface

## Technologies and design decisions
- **Kotlin + Android XML**: matches the starter Android Studio project.
- **Retrofit + Gson**: consumes the DummyJSON REST API (`https://dummyjson.com/`).
- **Coil**: third-party image-loading library used for remote product images and caching.
- **Android SDK / SharedPreferences**: persists the athlete's selected main sport locally.
- **Material Components + RecyclerView**: responsive, familiar Android UI.
- **JUnit**: automated tests for cart and favourites business logic.
- **GitHub Actions**: runs unit tests and builds the debug APK on pushes and pull requests.

## Architecture
`data/` contains the product model, REST API, repository and in-memory store. `ui/` contains the RecyclerView adapter. `MainActivity` coordinates navigation, loading, search and dialogs.

## REST API behaviour
The app requests men's clothing from DummyJSON. If the request fails, it automatically displays a small offline VISION catalogue so the prototype remains demonstrable without internet access.

## Running the app
1. Open the `Vision` folder in Android Studio.
2. Allow Gradle to sync dependencies.
3. Run on an Android emulator or physical Android device (minimum SDK 24).
4. Ensure internet access for live API products. The fallback catalogue works offline.

## Automated tests
Run locally with:
```bash
./gradlew testDebugUnitTest
```
GitHub Actions runs the same tests automatically and then builds the debug APK.

## GitHub workflow
Commit regularly rather than uploading the entire finished project in one commit. Suggested milestones: project setup, UI/navigation, REST API, favourites/cart, athlete profile, tests, README/documentation.

## Demo video checklist
Show: app launch; API-loaded catalogue; search; opening product details; adding/removing favourites; adding products to cart and viewing total; selecting an Athlete Profile sport; changing a visible setting or preference; then briefly show Retrofit, Coil, Store tests and the GitHub Actions workflow in Android Studio/GitHub.

## Part 1 alignment
The prototype implements the key VISION ideas identified in Part 1: product browsing, search, detailed information, favourites, cart/checkout, athlete-focused design and an Athlete Profile concept.
