Inditex Recogida Android
Technical documentation for the Inditex Recogida Android application. This project is a specialized industrial-grade scanning solution built on a multi-module Clean Architecture, optimized for PDA hardware and currently targeting the Spain region.

1. Project Architecture
   The application is architected into specific layers to enforce a strict unidirectional data flow and ensure that business logic remains independent of the UI or hardware implementation.

1.1 Module Definitions
:app The entry point of the application. Responsible for dependency injection (Koin) initialization and hosting the global Navigation Host.

:domain The core of the application. A pure Kotlin module containing business logic, UseCase definitions, and Repository interfaces. It has no dependencies on other modules.

:data Responsible for data orchestration. Implements Repository interfaces using Retrofit for network calls and SQLDelight for local persistence.

:data-core Infrastructure-level module handling hardware abstractions (PDA laser/haptics), base network configuration, and security cipher logic.

:feature-recogidas A feature-specific presentation module. Built with Jetpack Compose following the MVI (Model-View-Intent) pattern.

:core-common A shared utility module containing common UI components and threading abstractions like the DispatcherProvider.

:session Manages the authentication state, session lifecycles, and secure token storage.

2. Technical Stack
   Language: Kotlin 2.x

Concurrency: Coroutines and Flow

UI Framework: Jetpack Compose (Modern Declarative UI)

Dependency Injection: Koin

Persistence: SQLDelight (Type-safe SQL)

Networking: Retrofit 2 + OkHttp

Barcode Scanning: CameraX API + Google MLKit

Build System: Gradle Kotlin DSL + Version Catalog

3. Hardware Integration
   The application is optimized for specialized PDA (Personal Digital Assistant) hardware found in logistics environments.

Scanner Abstraction: The :data-core module provides an interface for both hardware laser scanning and camera-based scanning.

Haptic Feedback: Sophisticated vibration patterns are implemented using the VibratorManager for API 31+ and legacy Vibrator services for backward compatibility.

4. Testing Strategy
   Stability is ensured through a multi-layered testing suite:

Unit Testing: Comprehensive MockK-based testing for UseCase business rules and ViewModel state logic.

Reactive Verification: Using Turbine to test asynchronous StateFlow and Flow streams.

Database Verification: Local SQL testing using the JdbcSqliteDriver to run in-memory database tests.

UI Validation: Component-level verification using ComposeTestRule to ensure UI states (Loading, Error, Success) render correctly.

5. Deployment and Configuration
   5.1 Build Variants
   The project currently utilizes standard build types without country-specific flavors:

debug: Configured for development with extended logging and testing hooks.

release: Production-ready build with R8 shrinking and optimized performance.

5.2 Environment Setup
Standardize line endings to LF (Unix) via .editorconfig or IDE settings.

Synchronize the Version Catalog (libs.versions.toml) to ensure dependency consistency.

Generate the database interface via Gradle: ./gradlew :data:generateDebugRecogidaDatabaseInterface
