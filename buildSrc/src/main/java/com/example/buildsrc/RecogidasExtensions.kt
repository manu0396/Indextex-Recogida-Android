package com.example.buildsrc

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ApplicationExtension

val Project.libs: VersionCatalog get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

fun Project.configureRecogidasModule(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    val compileSdkVersion = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
    val minSdkVersion = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
    val targetSdkVersion = libs.findVersion("android-targetSdk").get().requiredVersion.toInt()
    val jvmVersion = libs.findVersion("jvmTarget").get().requiredVersion

    commonExtension.apply {
        compileSdk = compileSdkVersion

        defaultConfig {
            minSdk = minSdkVersion
            if (this is ApplicationDefaultConfig) {
                targetSdk = targetSdkVersion
            }
        }
        if (this is ApplicationExtension) {
            buildFeatures.buildConfig = true
        }
        if (this is LibraryExtension) {
            buildFeatures.buildConfig = true
        }
        compileOptions {
            sourceCompatibility = JavaVersion.toVersion(jvmVersion)
            targetCompatibility = JavaVersion.toVersion(jvmVersion)
        }

        flavorDimensions += listOf("market", "environment")
        productFlavors {
            create("spain") { dimension = "market" }

            create("mock") {
                dimension = "environment"
                buildConfigField("String", "ENVIRONMENT", "\"mock\"")
            }
            create("prod") {
                dimension = "environment"
                buildConfigField("String", "ENVIRONMENT", "\"prod\"")
            }
        }
    }

    tasks.withType(KotlinCompile::class.java).configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(jvmVersion))
        }
    }
}
