plugins {
    java
    id(BuildCatalog.Plugins.versions.id) version BuildCatalog.Plugins.versions.version
    id(BuildCatalog.Plugins.shadow.id) version BuildCatalog.Plugins.shadow.version
}

repositories {
    mavenCentral()
    maven("https://repo.gradle.org/gradle/libs-releases-local/")
}

dependencies {
    implementation("org.gradle:gradle-tooling-api:7.1.1")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.9")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
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
