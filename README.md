# Overview
A sample application showcasing two screens: "Products" and "Product Details." The project adheres to the MVVM pattern with a clean architecture approach, employing a single-module structure. Key libraries, such as Navigation Component, Hilt for dependency injection, Coroutines for asynchronous tasks, Retrofit for network calls, Coil for image loading, and Room for offline caching, are integrated to create a robust and modular Android application.

## Project Structure
The project is organized into four packages:
* di: Contains the dependency injection setup using Hilt.
* data: Handles data-related operations, including network requests, local storage, and data models.
* domain: Defines the business logic and use cases of the application.
* presentation: Houses the UI components, ViewModels, and navigation setup.


## Libraries Used
* Navigation Component: Used for seamless navigation between the "Products" and "Product Details" screens.
* Hilt: Employs dependency injection to enhance code organization and testability.
* Coroutines: Facilitates asynchronous operations for efficient background tasks.
* Retrofit: Makes network requests and interacts with a remote API to fetch product data.
* Coil: Efficiently loads images, contributing to a smooth user experience.
* Room: Facilitates offline caching, providing a local database for data storage and retrieval.
* ViewModel and LiveData: Manages UI-related data in a lifecycle-aware manner.
* ViewBinding: Reduces boilerplate code related to UI interactions.
* SwipeRefreshLayout: A library used for swipe layout to update data.