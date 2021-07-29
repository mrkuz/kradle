plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    kotlin("jvm") version "1.4.31"
    id("com.github.ben-manes.versions") version "0.39.0"
}

group = "net.bnb1.kradle"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Plugins
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21") // Determines the Kotlin version of the target project
    implementation("com.github.ben-manes:gradle-versions-plugin:0.39.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.4.30")
    implementation("org.jetbrains.kotlin:kotlin-allopen")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-plugin:0.3.1")
    implementation("com.adarshr:gradle-test-logger-plugin:3.0.0")
}

gradlePlugin {
    plugins {
        create("kradle") {
            id = "net.bnb1.kradle"
            implementationClass = "net.bnb1.kradle.KradlePlugin"
        }
    }
}

// Publish with alias 'kradle-gradle-plugin' (optional)
publishing {
    publications {
        create<MavenPublication>("kradle") {
            artifactId = "kradle-gradle-plugin"
            from(components["java"])
        }
    }
}
