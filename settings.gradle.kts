pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

rootProject.name = "currencybuddy"
include(":app")
include(":core:data")
include(":core:domain")
include(":core:datasource:remote")
include(":core:datasource:local")
include(":core:notification")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":converter:data")
include(":converter:domain")
include(":converter:presentation")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
