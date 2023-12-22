plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("net.bitsandbobs.kradle") version "main-SNAPSHOT"
}

group = "com.example"
version = "1.0.0"

kradle {
    kotlinJvmApplication {
        jvm {
            targetJvm("11")
            application {
                mainClass("com.example.demo.AppKt")
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
