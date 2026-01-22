buildscript {
    repositories {
        google()
        mavenCentral()
    }
    val tomlFile = file("../gradle/libs.versions.toml")
    val kotlinVersion = Regex("kotlin\\s*=\\s*\"(.*?)\"").find(tomlFile.readText())?.groupValues?.get(1)
        ?: error("Could not find 'kotlin' version in libs.versions.toml")

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-sam-with-receiver:$kotlinVersion")
    }
}

// Apply plugins manually
apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "org.jetbrains.kotlin.plugin.sam.with.receiver")
apply(plugin = "java-gradle-plugin")

repositories {
    google()
    mavenCentral()
}

fun getVersion(key: String): String {
    val tomlFile = file("../gradle/libs.versions.toml")
    return Regex("$key\\s*=\\s*\"(.*?)\"").find(tomlFile.readText())?.groupValues?.get(1) ?: ""
}

dependencies {
    implementation("com.android.tools.build:gradle:${getVersion("agp")}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${getVersion("kotlin")}")
    implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${getVersion("kotlin")}")
    implementation(gradleKotlinDsl())
    implementation(gradleApi())
}

configure<org.jetbrains.kotlin.samWithReceiver.gradle.SamWithReceiverExtension> {
    annotations("org.gradle.api.HasImplicitReceiver")
}
