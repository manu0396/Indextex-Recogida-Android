import com.example.buildsrc.configureRecogidasModule

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.core_common"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    configureRecogidasModule(this)
    androidComponents {
        beforeVariants { variantBuilder ->
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
    api(libs.kotlinx.coroutines.core)
    implementation(libs.google.mlkit.vision.common)
    implementation(libs.androidx.core.ktx)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.material3)
}
