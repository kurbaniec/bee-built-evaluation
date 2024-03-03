rootProject.name = "legacy.application"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") { from(files("../../bee-built/gradle/libs.versions.toml")) }
    }
}

includeBuild("../../bee-built/bee.generative")
includeBuild("../../bee-built/bee.fetched")
includeBuild("../../bee-built/bee.persistent")
includeBuild("../../bee-built/bee.functional")
includeBuild("../../bee-built/bee.buzz")


