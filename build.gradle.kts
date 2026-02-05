plugins {
    id(libs.plugins.android.application.get().pluginId) apply false
    id(libs.plugins.android.library.get().pluginId) apply false
    id(libs.plugins.kotlin.android.get().pluginId) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}
tasks.register<Copy>("installGitHooks") {
    description = "Copies the git hooks from scripts/ to .git/hooks"
    group = "git hooks"

    from(layout.projectDirectory.dir("scripts/pre-push.sh")) {
        rename { "pre-push" }
        filePermissions {
            user {
                read = true
                execute = true
                write = true
            }
            group {
                read = true
                execute = true
            }
            other {
                read = true
                execute = true
            }
        }
    }
    into(layout.projectDirectory.dir(".git/hooks"))
}
subprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin" && requested.name.startsWith("kotlin-stdlib")) {
                useVersion(libs.versions.kotlin.get())
            }
        }
    }
    afterEvaluate {
        if (extensions.findByName("android") != null) {
            configure<com.android.build.gradle.BaseExtension> {
                lintOptions {
                    lintConfig = rootProject.file("lint.xml")
                    isAbortOnError = false
                    isCheckReleaseBuilds = false
                }
            }
        }
    }
}
