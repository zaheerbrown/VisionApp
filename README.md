# VISION Sportswear App

VISION is an Android sportswear shopping application developed as part of the OPSC assignment.

The application provides users with a mobile shopping experience where they can browse sportswear products, search for products, add items to their favourites and cart, manage their athlete profile, and complete a prototype checkout process.

## Features

- Discover sportswear products
- Product images loaded from an online source
- Search and filter products
- View product information and prices
- Add and remove favourite products
- Shopping cart functionality
- Cart quantity and total calculations
- Athlete profile
- Prototype checkout functionality
- Bottom navigation between application screens

## Technologies Used

- Kotlin
- Android Studio
- XML layouts
- RecyclerView
- Material Design components
- Coil for image loading
- REST API integration
- JUnit for unit testing
- Git and GitHub for version control
- GitHub Actions for continuous integration

## Testing

The project includes local JUnit unit tests covering:

- Product price conversion
- Product category formatting
- Product search matching
- Invalid search matching
- Shopping cart total calculations

The tests can be run in Android Studio from:

`app/src/test/java/com/example/vision/ExampleUnitTest.kt`

## Running the Application

1. Clone the repository.
2. Open the project in Android Studio.
3. Allow Gradle to synchronize the project.
4. Connect an Android device or start an Android emulator.
5. Run the `app` configuration.
6. An internet connection is required for online product data and images.

## Project Structure

`app/src/main/java/com/example/vision/` contains the main Kotlin source code.

`app/src/main/res/` contains layouts, drawables, strings and other Android resources.

`app/src/test/` contains the unit tests.

## Prototype Demonstration Video

A demonstration of the VISION Sportswear App prototype can be viewed on YouTube:

https://youtube.com/shorts/ipIUnSOIXgI?feature=share

## Author

Developed by Zaheer Brown for the OPSC assignment.
