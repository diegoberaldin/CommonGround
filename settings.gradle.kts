pluginManagement {
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

rootProject.name = "CommonGround"
include(":app")

include(":core:appearance")
include(":core:architecture")
include(":core:cache")
include(":core:commonui")
include(":core:persistence")
include(":core:utils")

include(":domain:gallery")
include(":domain:image-fetch:data")
include(":domain:image-fetch:fetcher-api")
include(":domain:image-fetch:fetcher-impl")
include(":domain:image-fetch:lemmy")
include(":domain:image-fetch:repository")
include(":domain:image-fetch:usecase")

include(":feature:drawer")
include(":feature:imagelist")
include(":feature:imagedetail")
