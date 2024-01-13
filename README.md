# My Currency Exchange

My Currency Exchange is an Android app that simplifies currency conversion, allowing users to view a
given amount in a selected currency converted into other currencies. The app utilizes the free Open
Exchange Rates API to fetch the latest exchange rates.

## Features

- **Currency Conversion:** Enter an amount and select a currency to see its equivalent in other
  supported currencies.
- **Real-Time Data:** Exchange rates are fetched from the Open Exchange Rates service to provide
  up-to-date information.
- **Technologies Used:** ViewModel, LiveData, Hilt, Room, Architecture Components, Clean
  Architecture, Repository Pattern, Retrofit, Coroutines.

## Demo

<img src=demo.gif height="600px" alt="My Currency Exchange - Demo"/>

## How It Works

1. **ViewModel:** Manages UI-related data and communicates with the repository.
2. **Repository:** Handles data operations, fetching either from the local database (if data is less
   than 30 minutes old or there's an error in the remote) or the remote Open Exchange Rates API.
3. **Local Database (Room):** Persists exchange rates data locally.
4. **Remote API (Open Exchange Rates):** Provides the latest exchange rates.

## Technologies Used

- **Kotlin:** Programming language for Android development.
- **ViewModel and LiveData:** Architecture components for managing UI-related data.
- **Hilt:** Dependency injection library for Android.
- **Room:** Persistence library for local database.
- **Clean Architecture:** Organized project structure for better maintainability.
- **Retrofit:** HTTP client for making API calls.
- **Coroutines:** Asynchronous programming for efficient and responsive app behavior.
- **Unit Testing:** jUnit and mockk for testing components.

## Run Tests

To run the tests, you have two options:

1. Execute the following command in the terminal:
   `./gradlew test`
2. Right-click on the `test` folder in Android Studio, then select "Run Tests."

## Build and Run

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an Android emulator or physical device.

Voila! Enjoy exchanging currencies with My Currency Exchange!