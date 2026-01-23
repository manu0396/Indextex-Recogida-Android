plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.13.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    implementation("app.cash.sqldelight:gradle-plugin:2.0.2")
}
gradlePlugin {
    plugins {
        create("recogidasAndroid") {
            id = "recogidas.android"
            implementationClass = "com.example.buildsrc.RecogidasAndroidPlugin"
        }
    }
}
