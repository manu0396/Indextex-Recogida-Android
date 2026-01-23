pluginManagement {
    repositories {
        google()
        mavenCentral() // CRITICAL: SQLDelight is hosted here
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "InditexRecogidaAndroid"
include(":app", ":domain", ":data", ":data-core", ":core-common", ":recogidas-presentation", ":components", ":session",)
