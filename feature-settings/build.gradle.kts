plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.compose)
    id("recogidas.android")
}

android {
    namespace = "com.example.feature_settings"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":components"))
    implementation(project(":data-core"))
    implementation(project(":domain"))
    implementation(project(":core-common"))
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.inditex.ui)

    testImplementation(libs.test.junit)
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.androidx.test.junit)
}
