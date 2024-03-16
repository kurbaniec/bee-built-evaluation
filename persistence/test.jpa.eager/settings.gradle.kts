rootProject.name = "test.jpa.eager"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") { from(files("../../bee-built/gradle/libs.versions.toml")) }
    }
}

includeBuild("../../bee-built/bee.persistent")

// Common
include(":common")
project(":common").projectDir = File("../common")




