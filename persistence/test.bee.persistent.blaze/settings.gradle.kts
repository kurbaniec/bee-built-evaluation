rootProject.name = "test.bee.persistent.blaze"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") { from(files("../../bee-built/gradle/libs.versions.toml")) }
    }
}

includeBuild("../../bee-built/bee.generative")
includeBuild("../../bee-built/bee.persistent")

// Common
include(":common")
project(":common").projectDir = File("../common")




