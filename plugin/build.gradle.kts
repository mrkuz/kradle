import groovy.text.SimpleTemplateEngine
import org.eclipse.jgit.api.Git
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileWriter
import java.util.*

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id(Catalog.Build.Plugins.gradlePublish.id) version Catalog.Build.Plugins.gradlePublish.version
    id(Catalog.Build.Plugins.kotlinJvm.id) version Catalog.Build.Plugins.kotlinJvm.version
    id("net.bitsandbobs.kradle") version "2.1.0"
}

group = "net.bitsandbobs.kradle"
version = "main-SNAPSHOT"

buildscript {
    dependencies {
        classpath(Catalog.Build.Dependencies.jgit)
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(platform(Catalog.Build.Dependencies.Platform.kotlin))
    implementation(Catalog.Build.Dependencies.kotlinStdlib)
    implementation(Catalog.Build.Dependencies.jgit)

    // Plugins
    implementation(Catalog.Build.Dependencies.Plugins.kotlin)
    implementation(Catalog.Build.Dependencies.Plugins.allOpen)
    implementation(Catalog.Build.Dependencies.Plugins.kotlinSerialization)
    implementation(Catalog.Build.Dependencies.Plugins.dokka)
    implementation(Catalog.Build.Dependencies.Plugins.kotlinBenchmark)

    implementation(Catalog.Build.Dependencies.Plugins.testLogger)
    implementation(Catalog.Build.Dependencies.Plugins.shadow)
    implementation(Catalog.Build.Dependencies.Plugins.jib)
    implementation(Catalog.Build.Dependencies.Plugins.versions)
    implementation(Catalog.Build.Dependencies.Plugins.detekt)
    implementation(Catalog.Build.Dependencies.Plugins.ktlint)
    implementation(Catalog.Build.Dependencies.Plugins.owaspDependencyCheck)

    implementation(Catalog.Build.Dependencies.Plugins.spotbugs)

    // Testing
    testImplementation(Catalog.Build.Dependencies.Test.kotlinTest)
    testImplementation(Catalog.Build.Dependencies.Test.mockk)
    testImplementation(Catalog.Build.Dependencies.Test.dockerJava)
    Catalog.Build.Dependencies.Test.kotestBundle.forEach { testImplementation(it) }

    constraints {
        Catalog.Build.Constraints.ids.forEach {
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
        kotlin.enable()
        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        test {
            prettyPrint(true)
            integrationTests(true)
            withJunitJupiter()
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
    val generatedSources = project.buildDir.resolve("generatedSources/main/kotlin")
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
        java.srcDirs(project.buildDir.resolve("generatedSources/main/kotlin"))
    }
}

tasks.withType<KotlinCompile> {
    dependsOn("copyCatalog")
}

tasks.withType<Jar> {
    dependsOn("copyCatalog")
}

tasks.named("runKtlintCheckOverMainSourceSet") {
    dependsOn("copyCatalog")
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

gradlePlugin {
    plugins {
        create("kradle") {
            id = "net.bitsandbobs.kradle"
            implementationClass = "net.bnb1.kradle.plugins.KradlePlugin"
            displayName = "Kradle Plugin"
            description = "Swiss army knife for Kotlin/JVM (and also Java) development"
        }
        create("kradleApp") {
            id = "net.bitsandbobs.kradle-app"
            implementationClass = "net.bnb1.kradle.v1.KradleCompatAppPlugin"
            displayName = "Kradle App Plugin"
            description =
                "Swiss army knife for Kotlin/JVM development (deprecated, consider using 'net.bitsandbobs.kradle' instead)"
        }
        create("kradleLib") {
            id = "net.bitsandbobs.kradle-lib"
            implementationClass = "net.bnb1.kradle.v1.KradleCompatLibPlugin"
            displayName = "Kradle Lib Plugin"
            description =
                "Swiss army knife for Kotlin/JVM development (deprecated, consider using 'net.bitsandbobs.kradle' instead)"
        }
    }
}

pluginBundle {
    website = "https://github.com/mrkuz/kradle/tree/stable"
    vcsUrl = "https://github.com/mrkuz/kradle/tree/stable"
    tags =
        listOf(
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

/*
// For testing only
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(15))
    }
}
*/
