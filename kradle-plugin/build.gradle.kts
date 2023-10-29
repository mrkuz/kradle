import groovy.text.SimpleTemplateEngine
import org.eclipse.jgit.api.Git
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileWriter
import java.util.Properties

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id(BuildCatalog.Plugins.gradlePublish.id) version BuildCatalog.Plugins.gradlePublish.version
    id(BuildCatalog.Plugins.kotlinJvm.id) version BuildCatalog.Plugins.kotlinJvm.version
    id("net.bitsandbobs.kradle") version "2.4.1"
}

group = "net.bitsandbobs.kradle"
version = "main-SNAPSHOT"

buildscript {
    dependencies {
        classpath(BuildCatalog.Dependencies.jgit)
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(platform(BuildCatalog.Dependencies.Platform.kotlin))
    implementation(BuildCatalog.Dependencies.kotlinStdlib)
    implementation(BuildCatalog.Dependencies.jgit)

    // Plugins
    implementation(BuildCatalog.Dependencies.Plugins.kotlin)
    implementation(BuildCatalog.Dependencies.Plugins.allOpen) {
        exclude("org.jetbrains.kotlin", "kotlin-gradle-plugin-api")
    }
    implementation(BuildCatalog.Dependencies.Plugins.kotlinSerialization) {
        exclude("org.jetbrains.kotlin", "kotlin-gradle-plugin-api")
    }
    implementation(BuildCatalog.Dependencies.Plugins.dokka)
    implementation(BuildCatalog.Dependencies.Plugins.kotlinBenchmark)
    implementation(BuildCatalog.Dependencies.Plugins.testLogger)
    implementation(BuildCatalog.Dependencies.Plugins.shadow)
    implementation(BuildCatalog.Dependencies.Plugins.jib)
    implementation(BuildCatalog.Dependencies.Plugins.versions)
    implementation(BuildCatalog.Dependencies.Plugins.detekt)
    implementation(BuildCatalog.Dependencies.Plugins.ktlint)
    implementation(BuildCatalog.Dependencies.Plugins.owaspDependencyCheck)
    implementation(BuildCatalog.Dependencies.Plugins.kover)

    implementation(BuildCatalog.Dependencies.Plugins.spotbugs)

    // Testing
    testImplementation(BuildCatalog.Dependencies.Test.kotlinTest)
    testImplementation(BuildCatalog.Dependencies.Test.dockerJava)
    BuildCatalog.Dependencies.Test.kotestBundle.forEach { testImplementation(it) }

    constraints {
        BuildCatalog.Constraints.ids.forEach {
            api(it)
            implementation(it)
        }
    }
}

kradle {
    general {
        buildProperties.enable()
    }
    jvm {
        targetJvm("11")
        kotlin {
            lint {
                ktlint {
                    rules {
                        disable("no-wildcard-imports")
                    }
                }
            }
            codeAnalysis {
                detekt {
                    configFile("../detekt-config.yml")
                }
            }
            test {
                useMockk()
            }
        }
        dependencies.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        test {
            prettyPrint(true)
            showStandardStreams(true)
            withCustomTests("archUnit", "compat", "integration", "functional")
            useArchUnit()
            useTestcontainers()
        }
    }
}

// Not sure why this hack is needed
afterEvaluate {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

// Add configuration with dynamically added dependencies, so 'showDependencies' can check for updates
configurations {
    create("dynamic") {
        isVisible = false
        isTransitive = false
        Catalog.Dependencies.artifacts.forEach {
            dependencies.add(project.dependencies.create("${it.group}:${it.name}:${it.version}"))
        }
    }
}

tasks.register("copyCatalog").configure {
    val generatedSources = project.layout.buildDirectory.asFile.get().resolve("generatedSources/main/kotlin")
    outputs.dir(generatedSources)

    doFirst {
        val inputFile = project.rootDir.resolve("buildSrc/src/main/kotlin/Catalog.kt")
        val outputFile = generatedSources.resolve("net/bnb1/kradle/Catalog.kt")

        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            """
            package net.bnb1.kradle
            
            
            """.trimIndent()
        )
        outputFile.appendText(inputFile.readText())
    }
}

sourceSets {
    main {
        java.srcDirs(project.layout.buildDirectory.asFile.get().resolve("generatedSources/main/kotlin"))
    }
}

tasks.withType<KotlinCompile> {
    dependsOn("copyCatalog")
}

tasks.withType<Jar> {
    dependsOn("copyCatalog")
}

// Run functional tests with colima
/*
tasks.withType<Test> {
    environment("DOCKER_HOST", "unix:///Users/mrkuz/.colima/default/docker.sock")
    environment("TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE", "/var/run/docker.sock")
}
*/

tasks.named("runKtlintCheckOverMainSourceSet") {
    dependsOn("copyCatalog")
}

tasks.register<Copy>("buildAgent") {
    dependsOn(":kradle-agent:shadowJar")
    from(project(":kradle-agent").layout.buildDirectory.asFile.get().resolve("libs/kradle-agent-all.jar"))
    into(project.layout.buildDirectory.asFile.get().resolve("resources/main/"))
    rename { "kradle-agent.jar" }
}

tasks.named("processResources").configure {
    dependsOn("buildAgent")
}

tasks.register("renderTemplates").configure {
    doFirst {
        val properties = Properties()
        properties["gitBranch"] = Git.open(project.rootDir).repository.branch
        properties["kradleVersion"] = version
        properties["versions"] = Catalog.Versions

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

val tags = listOf(
    "kotlin",
    "java",
    "linting",
    "code-analysis",
    "dependency-analysis",
    "benchmarking",
    "documentation",
    "testing",
    "uberjar",
    "docker"
)

gradlePlugin {
    website.set("https://github.com/mrkuz/kradle/tree/stable")
    vcsUrl.set("https://github.com/mrkuz/kradle/tree/stable")
    plugins {
        create("kradle") {
            id = "net.bitsandbobs.kradle"
            implementationClass = "net.bnb1.kradle.plugins.KradlePlugin"
            displayName = "Kradle Plugin"
            description = "Swiss army knife for Kotlin/JVM (and also Java) development"
            tags.set(tags)
        }
        create("kradleApp") {
            id = "net.bitsandbobs.kradle-app"
            implementationClass = "net.bnb1.kradle.v1.KradleCompatAppPlugin"
            displayName = "Kradle App Plugin"
            description = "Swiss army knife for Kotlin/JVM development" +
                " (deprecated, consider using 'net.bitsandbobs.kradle' instead)"
            tags.set(tags)
        }
        create("kradleLib") {
            id = "net.bitsandbobs.kradle-lib"
            implementationClass = "net.bnb1.kradle.v1.KradleCompatLibPlugin"
            displayName = "Kradle Lib Plugin"
            description = "Swiss army knife for Kotlin/JVM development" +
                " (deprecated, consider using 'net.bitsandbobs.kradle' instead)"
            tags.set(tags)
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
