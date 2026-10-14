# My Currency Exchange

Android app that converts an amount in one currency into all others using the
[Open Exchange Rates](https://openexchangerates.org/) API. Rates are cached in Room for 30 minutes
and still shown offline.

<img src=demo.gif height="600px" alt="My Currency Exchange - Demo"/>

## Setup

Get a free app ID and add it to `local.properties` (git-ignored), or set it as an environment
variable:

```properties
OPEN_EXCHANGE_RATES_APP_ID=your_app_id
```

Then open the project in Android Studio and run the `app` configuration.

## Architecture

Clean Architecture with the repository pattern, split into `data` (Room, Retrofit, repository),
`domain` (models, use cases, repository interface), `di` (Hilt modules) and `ui` (Activity,
ViewModel, LiveData `ConverterUiState`).

## Tests

```
./gradlew test
```
