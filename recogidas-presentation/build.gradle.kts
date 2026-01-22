import com.example.buildsrc.configureRecogidasModule

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.recogidas_presentation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnit4Runner"
    }
    configureRecogidasModule(this)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kotlin {
        jvmToolchain(libs.versions.jvmTarget.get().toInt())
        jvmToolchain(21)
    }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":core-common"))
    implementation(project(":data-core"))
    implementation(project(":data"))
    implementation(project(":components"))

    // --- Navigation ---
    implementation(libs.androidx.navigation.compose)

    // --- UI & Lifecycle ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)

    // --- Lifecycle & Compose ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // --- DI (Koin) ---
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // --- CameraX & MLKit ---
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.google.mlkit.barcode)
    implementation(libs.google.mlkit.vision.common)
    implementation(libs.google.guava)

    // --- Camara utils ---
    implementation(libs.androidx.concurrent.futures.ktx)
    implementation(libs.google.guava)

    // --- Coroutines & Core ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)

    // Testing
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)
    testImplementation(libs.test.compose.ui.junit4)

    // UI Testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.test.compose.ui.manifest)
}


