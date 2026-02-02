plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("recogidas.android")
    id("app.cash.sqldelight")
}

sqldelight {
    databases {
        create("RecogidaDatabase") {
            packageName.set("com.example.data.db")
            dialect("app.cash.sqldelight:sqlite-3-38-dialect:2.0.2")
        }
    }
}

android {
    namespace = "com.example.data"
    lint {
        disable += "FlowOperatorInvokedInComposition"
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
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.test.mockk.android)
}
