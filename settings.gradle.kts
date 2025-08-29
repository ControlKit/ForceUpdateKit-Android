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
        maven {
            url = uri("https://jitpack.io")
            credentials {
                
                username = providers.gradleProperty("gpr.user").orNull ?: ""
                password = providers.gradleProperty("gpr.key").orNull ?: ""
            }
        }
    }
}

rootProject.name = "ForceUpdateKit"
include(":app")
include(":forceUpdatekit")