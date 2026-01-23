plugins {
    id("com.android.library")
    id("recogidas.android")
}

android {
    namespace = "com.example.core_common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)
    implementation(libs.google.material)

    // MLKit
    implementation(libs.google.mlkit.barcode)
    implementation(libs.google.mlkit.vision.common)

    // Tests
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)

    // Android Tests
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.material3)
}
