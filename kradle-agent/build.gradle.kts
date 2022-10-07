plugins {
    `java`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
    maven("https://repo.gradle.org/gradle/libs-releases-local/")
}

dependencies {
    implementation("org.gradle:gradle-tooling-api:7.1.1")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.36")
    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

tasks.named<Jar>("jar").configure {
    manifest {
        attributes(Pair("Premain-Class", "net.bnb1.kradle.Agent"))
    }
}
