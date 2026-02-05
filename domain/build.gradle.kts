plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("recogidas.android")
}

android {
    namespace = "com.example.domain"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    lint {
        disable.add("FlowOperatorInvokedInComposition")
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    api(project(":core-common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
