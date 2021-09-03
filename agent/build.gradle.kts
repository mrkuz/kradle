plugins {
    `java`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

tasks.named<Jar>("jar").configure {
    manifest {
        attributes(Pair("Premain-Class", "net.bnb1.stop.Agent"))
    }
}