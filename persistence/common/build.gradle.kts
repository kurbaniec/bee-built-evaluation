

plugins {
    alias(libs.plugins.kotlin.jvm)
}

allprojects {

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
}

group = "common"
version = libs.versions.bee.built.get()
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

ext {
    set("testcontainers.version", "1.19.7")
}

dependencies {
    implementation(kotlin("test"))

    implementation(libs.spring.boot.starter.test)
    implementation(libs.junit.api)
    implementation("org.openjdk.jmh:jmh-core:1.37")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.testcontainers:junit-jupiter:1.19.7")
    api("org.testcontainers:junit-jupiter:1.19.7")
    implementation("org.testcontainers:postgresql:1.19.7")
}

tasks.withType<Test> {
    useJUnitPlatform()
}




