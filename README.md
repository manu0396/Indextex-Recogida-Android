# PROJECT RECOGIDA ANDROID
**Logistics Scanning & Personnel Management Platform**

---

## 01. EXECUTIVE SUMMARY
This repository contains the core Android implementation for the Inditex Recogida platform. The system is engineered for industrial PDA hardware, utilizing a strict Clean Architecture pattern to ensure high-reliability scanning operations within the Spanish logistics network.

> [!IMPORTANT]
> This project is currently configured for the **Spain** environment and requires specific PDA hardware abstractions for laser scanning and haptic feedback.

---

## 02. ARCHITECTURAL BLUEPRINT
The application is partitioned into independent modules to enforce domain isolation and unidirectional data flow.



### MODULE MAP
| Module | Tier | Primary Responsibility |
| :--- | :--- | :--- |
| **:app** | Framework | Dependency Injection (Koin) & Navigation orchestration. |
| **:domain** | Core | Pure Kotlin Business Logic, UseCases, and Repository Contracts. |
| **:data** | Infrastructure | SQLDelight Persistence & Retrofit API implementations. |
| **:data-core** | Hardware | PDA Laser/Camera drivers and Haptic feedback logic. |
| **:feature-recogidas** | UI | Jetpack Compose views following the MVI state pattern. |
| **:session** | Auth | Secure token lifecycle and authentication state management. |

---

## 03. TECHNICAL SPECIFICATIONS
The platform leverages a modern reactive stack optimized for low-latency hardware interactions.

### CORE RUNTIME
* **Language:** Kotlin 2.x
* **Concurrency:** Coroutines + Flow (Reactive Streams)
* **DI Framework:** Koin (Service Locator)
* **Build System:** Gradle KTS + Version Catalog

### DATA & NETWORKING
* **Persistence:** SQLDelight (Type-safe SQLite)
* **Networking:** Retrofit 2 + OkHttp 4
* **Serialization:** GSON (Inditex API Standards)

### PERIPHERALS
* **Computer Vision:** CameraX + Google MLKit
* **Haptics:** VibratorManager (API 31+) & Legacy Vibrator support

---

## 04. HARDWARE ABSTRACTION LAYER
To maintain UI independence, hardware interactions are abstracted through the `:data-core` module.



* **Unified Scanning:** Provides a single entry point for both hardware laser engines and camera-based vision.
* **Industrial Haptics:** Implements specialized vibration patterns for noisy warehouse environments, with backward compatibility for legacy PDA models.

---

## 05. QUALITY ASSURANCE
A multi-tiered testing strategy ensures stability across mission-critical flows.

* **Domain Logic:** Unit tests using MockK.
* **State Verification:** Turbine for asynchronous Flow/StateFlow validation.
* **Persistence Integrity:** In-memory testing via JdbcSqliteDriver.
* **Component Testing:** Isolated UI validation using ComposeTestRule.

---

## 06. INITIALIZATION & DEPLOYMENT
Follow these steps to prepare the development environment.

### PRE-REQUISITES
1. Ensure your IDE is set to **LF (Unix)** line endings to prevent TOML parsing failures.
2. Verify `gradle/libs.versions.toml` is using `version.ref` for all declarations.

### BUILD COMMANDS
Generate the SQLDelight interfaces before the first compilation:
```bash
./gradlew :data:generateDebugRecogidaDatabaseInterface
