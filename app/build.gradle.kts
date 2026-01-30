plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("recogidas.android")
}

android {
    namespace = "com.example.pda_recogida_android"
}

dependencies {
    // Feature & Internal Modules
    implementation(project(":core-common"))
    implementation(project(":data-core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":session"))
    implementation(project(":components"))
    implementation(project(":recogidas-presentation"))

    // Tech Stack
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)

    // Android Tests
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
