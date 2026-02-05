plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("recogidas.android")
}

android {
    namespace = "com.example.session"
    lint {
        disable += "FlowOperatorInvokedInComposition"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core-common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.koin.android)
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
