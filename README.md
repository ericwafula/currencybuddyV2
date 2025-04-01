# Currency Buddy V2

**Currency Buddy V2** is an enhanced multi-module currency converter app for Android, built with Jetpack Compose and following modern best practices. The project has been re-architected for improved modularity, maintainability, and performance. Whether you’re looking for a quick conversion or interested in exploring the modular design, Currency Buddy V2 has you covered.

[![Build Status](https://img.shields.io/github/actions/workflow/status/ericwafula/currencybuddyV2/build.yml?branch=main)](https://github.com/ericwafula/currencybuddyV2/actions)  
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## Table of Contents

- [Tech Stack & Tools Used](#tech-stack--tools-used)
- [Features](#features)
- [Architecture & Modularization](#architecture--modularization)
- [Widget Integration](#widget-integration)
- [Getting Started](#getting-started)
- [Contributing](#contributing)
- [Support](#support)
- [License](#license)

---

## Tech Stack & Tools Used

- **Kotlin** — Modern Android-first language
- **Jetpack Compose** — Declarative UI toolkit
- **Room Database** — Local persistence
- **WorkManager** — Background task scheduling
- **Ktor Client** — Asynchronous networking
- **Koin** — Dependency injection framework
- **Coroutines + Flow** — Reactive streams and concurrency
- **DataStore** — Preferences and proto data storage
- **Glance API** — Jetpack-based App Widgets
- **Timber** — Structured logging
- **JUnit5 & MockK** — Testing framework
- **Firebase (Crashlytics & Analytics)** — Error monitoring and analytics

---

## Features

- 🔄 Real-time and cached currency conversion
- 🧱 Fully modular architecture
- ✨ App Widget integration with Jetpack Glance
- 📴 Offline-first currency support
- ✅ Extensible widget update services

---

## Architecture & Modularization

<img src="screenshots/architecture.jpeg">

Currency Buddy V2 is structured around layered and feature-based modularization:

### 🧩 Modules

- `:core:data` — Network & local data logic
- `:core:domain` — Business logic, interfaces, and use cases
- `:core:local` — Room + DataStore implementations
- `:core:remote` — Ktor API layer
- `:core:presentation:designsystem` — Shared design components
- `:core:presentation:ui` — Common UI helpers
- `:converter:data`, `:converter:domain`, `:converter:presentation` — Feature-specific logic
- `:auth:` — Onboarding and authentication (coming soon)
- `:widget:presentation` — Glance-powered widget integration

### 🔄 App Widget (Glance)
- Uses `GlanceAppWidget` and `GlanceAppWidgetReceiver`
- Updates in real-time using flows and repository pattern
- Integrates with WorkManager for background updates
- Separates widget logic via:
    - `ConverterWidgetUpdater` — focused updater interface
    - `WidgetUpdater` — generic interface for multiple widgets
    - `DefaultConverterWidgetUpdater` — image download, URI creation, widget binding
- Handles SVG image rendering using Coil + FileProvider
- Shown placeholder using `android:initialLayout`

---

## Widget Integration

Widgets follow clean principles:

- **Interface Segregation:** Small, dedicated interfaces for each widget
- **Dependency Injection:** Updaters are injected via Koin
- **Composable Architecture:** Widget state updates via `DataStore` & `updateAppWidgetState`
- **Boot Resilience:** `WorkManager` continues syncing even after device reboots
- **Glance Placeholder:** `android:initialLayout` includes helpful text + app logo
- **Optimized Image Caching:** Images are downloaded and served via `FileProvider` and Coil

### Widget Sync Flow:
```
WorkManager/Repository emits -> ViewModel or Worker collects -> WidgetUpdater updates state -> Widget renders
```

---

## Getting Started

### Prerequisites
- Android Studio (latest)
- API key and endpoints defined in your `.env` or `local.properties` file:
    - `API_KEY`
    - `CONVERTER_BASE_URL`
    - `CURRENCY_DETAILS_URL`

### Installation

```bash
git clone https://github.com/ericwafula/currencybuddyV2.git
cd currencybuddyV2
```

Open in Android Studio and run on a device or emulator.

---

## Contributing

We welcome contributions to Currency Buddy! Feel free to open issues, fork the repo, and submit PRs. See [CONTRIBUTING.md](CONTRIBUTING.md) for more details.

---

## Support
If you find this project useful, consider starring ⭐ it or buying me a coffee ☕.

---

## License
This project is licensed under the [MIT License](LICENSE).
