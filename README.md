# AssetDash

**AssetDash** is a modern, offline-first cryptocurrency portfolio tracker built with **Jetpack Compose** and **Clean Architecture**.

It allows users to track their crypto assets, visualize price history, and calculate real-time profit/loss based on their transaction history. The app follows the **Single Source of Truth (SSOT)** principle, ensuring full functionality even without an internet connection.

## Key Features

* **Real-time Market Data:** Fetches live cryptocurrency data from CoinGecko API.
* **Portfolio Tracking:** Calculates total net worth, average buy price, and P/L (Profit & Loss) based on user transactions.
* **Offline-First Architecture:** Uses **Room Database** as the single source of truth. The app works perfectly offline and syncs when online.
* **Interactive Charts:** Visualizes 7-day price history using **Vico** charting library.
* **Background Synchronization:** Utilizes **WorkManager** to keep market data fresh periodically in the background.
* **Watchlist:** Allows users to favorite specific assets for quick access.
* **Smart Search:** Filterable dropdown for selecting assets during transaction input.

## Tech Stack & Libraries

* **Language:** Kotlin
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Architecture:** MVVM + Clean Architecture (Presentation, Domain, Data layers)
* **Dependency Injection:** [Hilt](https://dagger.dev/hilt/)
* **Asynchronous:** Coroutines & Flow
* **Local Storage:** [Room](https://developer.android.com/training/data-storage/room)
* **Network:** Retrofit + OkHttp
* **Background Tasks:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) (with Hilt integration)
* **Charting:** [Vico](https://github.com/patrykandpatrick/vico)
* **Image Loading:** Coil
* **Testing:** JUnit4, MockK, Turbine, Coroutines Test

## Architecture

This app adopts a **Clean Architecture** approach with **MVVM** pattern, separating concerns into three distinct layers:

1.  **Presentation Layer (UI):** Contains Composables and ViewModels. ViewModels expose `StateFlow` to the UI and handle user events.
2.  **Domain Layer:** Contains pure Kotlin logic (Entities, Use Cases, Repository Interfaces). This layer has no Android dependencies.
3.  **Data Layer:** Handles data retrieval from Local (Room) and Remote (Retrofit) sources. Implements the **NetworkBoundResource** pattern to manage caching and offline support.

```mermaid
graph TD
    UI[Compose UI] --> VM[ViewModel]
    VM --> UC[Use Cases]
    UC --> Repo[Repository Interface]
    RepoImpl[Repository Implementation] --> Repo
    RepoImpl --> Local[Room Database]
    RepoImpl --> Remote[Retrofit API]
    Background[WorkManager] --> RepoImpl