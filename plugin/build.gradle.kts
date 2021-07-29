plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("org.gradle.kotlin.kotlin-dsl") version "2.1.6"
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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    implementation("com.github.ben-manes:gradle-versions-plugin:0.38.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.4.30")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-plugin:0.3.1")
}

gradlePlugin {
    plugins {
        create("kradle") {
            id = "net.bnb1.kradle"
            implementationClass = "net.bnb1.kradle.KradlePlugin"
        }
    }
}

// Optional: Publish with alias 'kradle-gradle-plugin'
publishing {
    publications {
        create<MavenPublication>("kradle") {
            artifactId = "kradle-gradle-plugin"
            from(components["java"])
        }
    }
}
