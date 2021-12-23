plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("net.bitsandbobs.kradle-app") version "main-SNAPSHOT"
}

group = "com.example"
version = "1.0.0"

kradle {
    targetJvm("17")
    mainClass("com.example.demo.App", jvmName = false)
    // kotlinxCoroutinesVersion("1.6.0")
    // ktlintVersion("0.43.2")
    // detektVersion("1.19.0")
    // jmhVersion("1.34")
    tests {
        // junitJupiterVersion("5.8.2")
        // jacocoVersion("0.8.7")
        useKotest("5.0.3")
        useMockk("1.12.1")
    }
    uberJar {
        minimize(true)
    }
    image {
        // baseImage("bellsoft/liberica-openjdk-alpine:17")
        // ports.add(8080)
        // jvmOpts("-Xmx512M")
        // withAppSh()
        withJvmKill("1.16.0")
    }
}
