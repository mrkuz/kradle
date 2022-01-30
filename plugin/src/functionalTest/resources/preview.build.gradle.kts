plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("net.bitsandbobs.kradle") version "main-SNAPSHOT"
}

group = "com.example"
version = "1.0.0"

kradle {
    javaApplication {
        jvm {
            targetJvm("15")
            application {
                mainClass("com.example.demo.App")
            }
            java {
                previewFeatures(true)
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(15))
    }
}
