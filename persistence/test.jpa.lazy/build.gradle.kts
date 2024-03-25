

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencymanagement)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.noarg)
    java
}

allprojects {

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.register<Task>(name = "resolveDependencies") {
        group = "Build Setup"
        description = "Resolve and prefetch dependencies"
        doLast {
            rootProject.allprojects.forEach {
                it.buildscript.configurations.filter(Configuration::isCanBeResolved).forEach { it.resolve() }
                it.configurations.filter(Configuration::isCanBeResolved).forEach { it.resolve() }
            }
        }
    }
}

group = "test"
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

sourceSets {
    create("jmh") {
        java {
            srcDirs("src/jmh/kotlin", "src/jmh/java")
            compileClasspath += main.get().output + test.get().output
            runtimeClasspath += main.get().output + test.get().output
            configurations["jmhImplementation"].extendsFrom(
                configurations.implementation.get(),
                configurations.testImplementation.get()
            )
            configurations["jmhRuntimeOnly"].extendsFrom(
                configurations.runtimeOnly.get(),
                configurations.testRuntimeOnly.get()
            )
        }
    }
}

ext {
    set("testcontainers.version", "1.19.7")
}

dependencies {
    implementation(project(":common"))
    implementation("com.beeproduced:bee.persistent")
    implementation("com.beeproduced:bee.persistent") {
        capabilities { requireCapability("com.beeproduced:bee.persistent-jpa") }
    }

    // in-house libraries
    /* implementation("com.beeproduced:bee.buzz")
    implementation("com.beeproduced:bee.buzz") {
        capabilities { requireCapability("com.beeproduced:bee.buzz-simple") }
    }
    implementation("com.beeproduced:bee.functional") {
        capabilities { requireCapability("com.beeproduced:bee.functional-dgs") }
    }
    implementation("com.beeproduced:bee.persistent")
    implementation("com.beeproduced:bee.persistent") {
        capabilities { requireCapability("com.beeproduced:bee.persistent-dgs") }
    } */
    // external dependencies
    implementation(libs.kotlin.stdlib)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.websocket)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.cache)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.boot.starter.oauth2.client)
    implementation(libs.spring.security.config)
    implementation(libs.spring.security.messaging)
    implementation(libs.spring.security.web)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.konform)
    implementation(libs.datafaker)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.data.jpa)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.junit.api)
    testImplementation(libs.kotlin.test)
    testRuntimeOnly(libs.junit.engine)
    implementation(libs.h2)
    testImplementation(libs.springmockk)

    implementation("org.postgresql:postgresql:42.7.3")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")


    if (System.getProperty("os.arch") == "aarch64" && System.getProperty("os.name") == "Mac OS X") {
        runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.76.Final:osx-aarch_64")
    }

    implementation("net.bytebuddy:byte-buddy:1.14.9")
    implementation("net.bytebuddy:byte-buddy-agent:1.14.9")

    "jmhImplementation"("org.openjdk.jmh:jmh-core:1.37")
    "jmhAnnotationProcessor"("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    "kaptJmh"("org.openjdk.jmh:jmh-generator-annprocess:1.37")

}

tasks.withType<Test> {
    useJUnitPlatform()
}


tasks.getByName<Jar>("jar") {
    enabled = false
}


tasks.bootRun {
    jvmArgs = listOf("-Dspring.output.ansi.enabled=ALWAYS")
}

tasks.register<Test>("jmhBenchmark") {
    dependsOn(tasks.getByName("jmhClasses"))
    testClassesDirs = sourceSets["jmh"].output.classesDirs
    classpath = sourceSets["jmh"].runtimeClasspath
    val dataSize = if (project.hasProperty("dataSize"))
        project.property("dataSize") as String
    else "500"
    systemProperty("dataSize", dataSize)

    filter {
        includeTestsMatching("test.jpa.lazy.CinemaBenchmarkJpaLazy")
    }
}
