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
        val contextUrl = providers.gradleProperty("artifactory_context_url").orNull
        val repoKey = providers.gradleProperty("artifactory_resolve_repokey").orNull
        val user = providers.gradleProperty("artifactory_username").orNull
        val pass = providers.gradleProperty("artifactory_password").orNull
        if (contextUrl != null && repoKey != null) {
            maven {
                url = uri("$contextUrl/$repoKey")
                credentials {
                    username = user
                    password = pass
                }
            }
        }
    }
}

rootProject.name = "InditexRecogidaAndroid"
include(
    ":app",
    ":domain",
    ":data",
    ":data-core",
    ":core-common",
    ":recogidas-presentation",
    ":components",
    ":session",
    ":feature-settings"
)
