plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("RecogidaDatabase") {
            packageName.set("com.example.data.db")
        }
    }
}

android {
    namespace = "com.example.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmToolchain(libs.versions.jvmTarget.get().toInt())
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
            freeCompilerArgs.add("-Xjdk-release=21")
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core-common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.koin.android)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.gson)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.sqldelight.android.driver)
    implementation(libs.sqldelight.coroutines)
    // Testing
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)
    testImplementation(libs.test.okhttp.mockwebserver)
    testImplementation(libs.test.sqldelight.driver)
    // For instrumented hardware tests
    androidTestImplementation(libs.test.mockk.android)
    androidTestImplementation(libs.androidx.junit)
}
