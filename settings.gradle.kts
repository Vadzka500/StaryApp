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

rootProject.name = "NavWithApiNothing_2"
include(":app")

include(":feature")
include(":data")


include(":core")
include(":domain")
include(":feature:account")
include(":feature:account:data")
include(":feature:account:domain")
include(":feature:account:presentation")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":core:ui")
include(":feature:bookmark")
include(":feature:bookmark:data")
include(":feature:bookmark:domain")
include(":feature:bookmark:presentation")
include(":feature:viewed")
include(":feature:viewed:data")
include(":feature:viewed:domain")
include(":feature:viewed:presentation")
include(":feature:folder")
include(":feature:folder:data")
include(":feature:folder:domain")
include(":feature:folder:presentation")
include(":feature:collectionmovies")
include(":feature:collectionmovies:data")
include(":feature:collectionmovies:domain")
include(":feature:collectionmovies:presentation")
include(":feature:search")
include(":feature:search:data")
include(":feature:search:domain")
include(":feature:search:presentation")
include(":feature:random")
include(":feature:random:data")
include(":feature:random:domain")
include(":feature:random:presentation")
include(":feature:folders")
include(":feature:folders:data")
include(":feature:folders:domain")
include(":feature:folders:presentation")
include(":feature:home")
include(":feature:home:data")
include(":feature:home:domain")
include(":feature:home:presentation")
include(":feature:review")
include(":feature:review:data")
include(":feature:review:domain")
include(":feature:review:presentation")
include(":feature:movie")
include(":feature:movie:data")
include(":feature:movie:domain")
include(":feature:movie:presentation")
include(":feature:person")
include(":feature:person:data")
include(":feature:person:domain")
include(":feature:person:presentation")
include(":feature:collections")
include(":feature:collections:data")
include(":feature:collections:domain")
include(":feature:collections:presentation")
include(":core:navigation")
