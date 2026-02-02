plugins {
    alias(libs.plugins.kotlin.compose) apply false
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
