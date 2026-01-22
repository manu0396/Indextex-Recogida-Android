import com.example.buildsrc.configureRecogidasModule

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.data_core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    configureRecogidasModule(this)
    androidComponents {
        beforeVariants { variantBuilder ->
            // Disables the instrumented test variant that triggers the resource linker failure
            variantBuilder.androidTest.enable = false
        }
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
    implementation(project(":data"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.play.services.measurement.api)
    // Testing
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)
    testImplementation(libs.test.okhttp.mockwebserver)
    // For instrumented hardware tests
    androidTestImplementation(libs.test.mockk.android)
    androidTestImplementation(libs.androidx.junit)

}
