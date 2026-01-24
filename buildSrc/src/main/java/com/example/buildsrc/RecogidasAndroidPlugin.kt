package com.example.buildsrc

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class RecogidasAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.android")
            extensions.configure(KotlinAndroidProjectExtension::class.java) {
                jvmToolchain(21)
            }
            tasks.withType(KotlinJvmCompile::class.java).configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)
                }
            }

            pluginManager.withPlugin("com.android.application") {
                extensions.configure(ApplicationExtension::class.java) {
                    configureCommonAndroid(this)
                    configureApplicationSpecifics(this)
                }
            }

            pluginManager.withPlugin("com.android.library") {
                extensions.configure(LibraryExtension::class.java) {
                    configureCommonAndroid(this)
                }
            }

            pluginManager.withPlugin("app.cash.sqldelight") {
                val sqlExtension = extensions.getByName("sqldelight")
                try {
                    val getDatabasesMethod = sqlExtension.javaClass.getMethod("getDatabases")
                    val databases = getDatabasesMethod.invoke(sqlExtension) as org.gradle.api.NamedDomainObjectContainer<*>
                    databases.configureEach {
                        val getSrcDirsMethod = this.javaClass.getMethod("getSrcDirs")
                        val srcDirs = getSrcDirsMethod.invoke(this) as org.gradle.api.file.ConfigurableFileCollection
                        srcDirs.setFrom("src/main/sqldelight")
                    }
                } catch (e: Exception) {
                    logger.warn("SQLDelight: No se pudo configurar automáticamente." + e.message)
                }
            }
        }
    }

    private fun Project.configureCommonAndroid(extension: CommonExtension<*, *, *, *, *, *>) {
        val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        val compileSdkVer = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        val targetSdkVer = libs.findVersion("android-targetSdk").get().requiredVersion.toInt()
        extension.apply {
            compileSdk = compileSdkVer

            defaultConfig {
                minSdk = 26
                if (this is ApplicationDefaultConfig) {
                    targetSdk = targetSdkVer
                }
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }

            buildFeatures { buildConfig = true }

            flavorDimensions.clear()
            flavorDimensions += "environment"

            productFlavors {
                maybeCreate("spainPre").apply {
                    dimension = "environment"
                    buildConfigField("String", "BASE_URL", "\"https://pre.inditex.com/\"")
                }
                maybeCreate("spainPro").apply {
                    dimension = "environment"
                    buildConfigField("String", "BASE_URL", "\"https://api.inditex.com/\"")
                }
                maybeCreate("mock").apply {
                    dimension = "environment"
                    buildConfigField("String", "BASE_URL", "\"http://localhost/\"")
                }
            }
        }
        pluginManager.withPlugin("com.android.application") {
            extensions.findByType(ApplicationExtension::class.java)?.let { appExt ->
                appExt.productFlavors.configureEach {
                    if (name == "spainPre") {
                        applicationIdSuffix = ".pre"
                    }
                }
            }
        }
    }
    private fun Project.configureApplicationSpecifics(extension: ApplicationExtension) {
        extension.productFlavors.getByName("spainPre").apply {
            applicationIdSuffix = ".pre"
        }
    }
}
