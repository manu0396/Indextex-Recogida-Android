pluginManagement {
    repositories {
        google()
        mavenCentral()
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

include(":app")
include(":data-core")
include(":data")
include(":domain")
include(":session")
include(":components")
include(":recogidas-presentation")
include(":core-common")
