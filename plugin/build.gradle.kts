import groovy.text.SimpleTemplateEngine
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileWriter
import java.util.*

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id(Catalog.Plugins.gradlePublish.id) version Catalog.Plugins.gradlePublish.version
    id(Catalog.Plugins.kotlinJvm.id) version Catalog.Plugins.kotlinJvm.version
    id(Catalog.Plugins.testLogger.id) version Catalog.Plugins.testLogger.version
}

group = "net.bitsandbobs.kradle"
version = "main-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(platform(Catalog.Dependencies.Platform.kotlin))
    implementation(Catalog.Dependencies.kotlinStdlib)
    implementation(Catalog.Dependencies.jgit)

    // Plugins
    implementation(Catalog.Dependencies.Plugins.kotlin)
    implementation(Catalog.Dependencies.Plugins.allOpen)
    implementation(Catalog.Dependencies.Plugins.kotlinSerialization)
    implementation(Catalog.Dependencies.Plugins.dokka)
    implementation(Catalog.Dependencies.Plugins.kotlinBenchmark)

    implementation(Catalog.Dependencies.Plugins.testLogger)
    implementation(Catalog.Dependencies.Plugins.shadow)
    implementation(Catalog.Dependencies.Plugins.jib)
    implementation(Catalog.Dependencies.Plugins.versions)
    implementation(Catalog.Dependencies.Plugins.detekt)
    implementation(Catalog.Dependencies.Plugins.ktlint)
    implementation(Catalog.Dependencies.Plugins.owaspDependencyCheck)

    // Testing
    testImplementation(Catalog.Dependencies.Test.kotlinTest)
    testImplementation(Catalog.Dependencies.Test.dockerJava)
    Catalog.Dependencies.Test.kotestBundle.forEach { testImplementation(it) }
}

sourceSets {
    main {
        java.srcDirs(project.rootDir.resolve("buildSrc/src/main/kotlin"))
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

tasks.register<Copy>("buildAgent") {
    dependsOn(":agent:jar")
    from(project(":agent").buildDir.resolve("libs/agent.jar"))
    into(project.buildDir.resolve("resources/main/"))
}

tasks.named("processResources").configure {
    dependsOn("buildAgent")
}

tasks.register("renderTemplates").configure {
    doFirst {
        val properties = Properties()
        project.rootDir.resolve("template.properties").inputStream().use { properties.load(it) }
        properties["kradleVersion"] = version

        val engine = SimpleTemplateEngine()
        project.fileTree(project.rootDir)
            .matching { include("**/*.in") }
            .forEach { file ->
                val name = file.name.replace(Regex("\\.in$"), "")
                val output = file.parentFile.resolve(name)
                println("Rendering ${output.absolutePath}")
                FileWriter(output).use { writer ->
                    engine.createTemplate(file).make(properties).writeTo(writer)
                }
            }
    }
}

gradlePlugin {
    plugins {
        create("kradle") {
            id = "net.bitsandbobs.kradle"
            implementationClass = "net.bnb1.kradle.plugins.KradlePlugin"
            displayName = "Kradle Plugin"
            description = "Swiss army knife for Kotlin/JVM development"
        }
        create("kradleApp") {
            id = "net.bitsandbobs.kradle-app"
            implementationClass = "net.bnb1.kradle.v1.KradleCompatAppPlugin"
            displayName = "Kradle App Plugin"
            description = "Swiss army knife for Kotlin/JVM development"
        }
        create("kradleLib") {
            id = "net.bitsandbobs.kradle-lib"
            implementationClass = "net.bnb1.kradle.v1.KradleCompatLibPlugin"
            displayName = "Kradle Lib Plugin"
            description = "Swiss army knife for Kotlin/JVM development"
        }
    }
}

pluginBundle {
    website = "https://github.com/mrkuz/kradle"
    vcsUrl = "https://github.com/mrkuz/kradle"
    tags =
        listOf(
            "kotlin",
            "linting",
            "code-analysis",
            "dependency-analysis",
            "benchmarking",
            "documentation",
            "testing",
            "uberjar",
            "docker"
        )
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
