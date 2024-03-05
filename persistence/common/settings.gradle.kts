rootProject.name = "common"

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

// Datasources
include(":datasource.a")
project(":datasource.a").projectDir = File("./datasource.a")
include(":datasource.b")
project(":datasource.b").projectDir = File("./datasource.b")
include(":datasource.test")
project(":datasource.test").projectDir = File("./datasource.test")


