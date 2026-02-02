plugins {
    id("com.android.library")
    id("recogidas.android")
}

android {
    namespace = "com.example.data_core"
    androidComponents {
        beforeVariants { variantBuilder ->
            variantBuilder.androidTest.enable = false
        }
    }
    lint {
        disable += "FlowOperatorInvokedInComposition"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core-common"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.play.services.measurement.api)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)
    testImplementation(libs.test.okhttp.mockwebserver)

    // Android Tests
    androidTestImplementation(libs.test.mockk.android)
    androidTestImplementation(libs.androidx.test.junit)
}
