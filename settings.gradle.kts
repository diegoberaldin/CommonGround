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
include(":core:commonui")
include(":core:persistence")
include(":core:utils")

include(":domain:gallery")
include(":domain:favorites")
include(":domain:image-fetch:cache")
include(":domain:image-fetch:data")
include(":domain:image-fetch:fetcher-api")
include(":domain:image-fetch:fetcher-impl")
include(":domain:image-fetch:lemmy")
include(":domain:image-source:data")
include(":domain:image-source:repository")
include(":domain:image-source:usecase")
include(":domain:palette")

include(":feature:drawer")
include(":feature:favorites")
include(":feature:imagelist")
include(":feature:imagedetail")
include(":feature:settings")
include(":feature:settings:config-sources")
