plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("recogidas.android")
}

android {
    namespace = "com.example.session"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
