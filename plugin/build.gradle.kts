import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import groovy.text.SimpleTemplateEngine
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileWriter
import java.util.*

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.16.0"
    kotlin("jvm") version "1.5.21"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.adarshr.test-logger") version "3.0.0"
}

group = "net.bitsandbobs.kradle"
version = "main-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Plugins
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.5.30")
    implementation("org.jetbrains.kotlin:kotlin-allopen")
    implementation("org.jetbrains.kotlin:kotlin-serialization")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-plugin:0.3.1")

    implementation("com.adarshr:gradle-test-logger-plugin:3.0.0")
    implementation("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:7.0.0")
    implementation("gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:3.1.4")
    implementation("com.github.ben-manes:gradle-versions-plugin:0.39.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.18.1")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.2.0")
    implementation("org.owasp:dependency-check-gradle:6.3.1")

    // Miscellaneous
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.13.0.202109080827-r")

    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.3")
    testImplementation("io.kotest:kotest-property:4.6.3")
    testImplementation("com.github.docker-java:docker-java:3.2.12")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<DependencyUpdatesTask> {
    revision = "release"
    checkForGradleUpdate = true
    // Exclude milestones and RCs
    rejectVersionIf {
        val alpha = "^.*-alpha[.-]?[0-9]*$".toRegex()
        val milestone = "^.*[.-]M[.-]?[0-9]+$".toRegex()
        val releaseCandidate = "^.*-RC[.-]?[0-9]*$".toRegex()
        alpha.matches(candidate.version)
                || milestone.matches(candidate.version)
                || releaseCandidate.matches(candidate.version)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
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
        create("kradleApp") {
            id = "net.bitsandbobs.kradle-app"
            implementationClass = "net.bnb1.kradle.plugins.KradleAppPlugin"
            displayName = "Kradle App Plugin"
            description = "Swiss army knife for Kotlin/JVM development"
        }
        create("kradleLib") {
            id = "net.bitsandbobs.kradle-lib"
            implementationClass = "net.bnb1.kradle.plugins.KradleLibPlugin"
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