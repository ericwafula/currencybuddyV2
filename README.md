# Currency Buddy V2

**Currency Buddy V2** is an enhanced multi-module currency converter app for Android, built with Jetpack Compose and following modern best practices. The project has been re-architected for improved modularity, maintainability, and performance. Whether you’re looking for a quick conversion or interested in exploring the modular design, Currency Buddy V2 has you covered.

[![Build Status](https://img.shields.io/github/actions/workflow/status/ericwafula/currencybuddyV2/build.yml?branch=main)](https://github.com/ericwafula/currencybuddyV2/actions)  
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## Table of Contents

- [Features](#features)
- [Architecture & Modularization](#architecture--modularization)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the App](#running-the-app)
- [Dependencies & Tools](#dependencies--tools)
- [Contributing](#contributing)
- [Support](#support)
- [License](#license)

---

## Features

- **Intuitive UI:** A clean and simple interface powered by Jetpack Compose for effortless currency conversion.
- **Modular Design:** Separation of concerns through a layered architecture combined with feature-based modularization.
- **Accurate Conversions:** Real-time currency conversion using up-to-date exchange rates.
- **Best Practices:** Updated to leverage modern Android development techniques and libraries for improved performance and maintainability.

---

## Architecture & Modularization

Currency Buddy V2 has been designed with a focus on modularity and separation of concerns. The project follows both layered and feature-based modularization strategies:

- **Core Module:**
    - **Data Layer:** Handles data retrieval, caching, and network operations.
    - **Domain Layer:** Contains business logic and use cases.
    - **Presentation Layers:**
        - `presentation:ui` for UI components that can be shared across features.
        - `presentation:designsystem` for reusable UI elements and theming.

- **Current Feature Modules:**
    - **converter:** Focuses on the currency conversion feature, with its own set of layers to keep functionality isolated and testable.
  
**Coming Soon:**
    - **auth:** Manages the onboarding flow with dedicated layers for data, domain, and presentation.

This structure not only helps in maintaining clear boundaries between different parts of the app but also paves the way for easier scalability and testing.

---

## Getting Started

### Prerequisites

- **Android Studio:** Ensure you have the latest version installed.
- **Android SDK:** Up-to-date SDK tools for the API level targeted by the project.
- **Environment Variables:**  
  The app requires the following environment variables to be set:
    - `API_KEY`: Your API key for currency conversion: This is just a placeholder at the moment.  
      Example: `API_KEY=abc`
    - `CONVERTER_BASE_URL`: Base URL for fetching currency conversion data.  
      Example: `CONVERTER_BASE_URL=https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/`
    - `CURRENCY_DETAILS_URL`: URL for obtaining additional currency details.  
      Example: `CURRENCY_DETAILS_URL=https://ericwafula.github.io/currency-details/currencies.json`

### Installation

1. **Fork and Clone the Repository:**

   ```bash
   git clone https://github.com/ericwafula/currencybuddyV2.git
   cd currencybuddyV2
