import groovy.text.SimpleTemplateEngine
import org.eclipse.jgit.api.Git
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileWriter
import java.util.*

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id(Catalog.BuildPlugins.gradlePublish.id) version Catalog.BuildPlugins.gradlePublish.version
    id(Catalog.BuildPlugins.kotlinJvm.id) version Catalog.BuildPlugins.kotlinJvm.version
    id("net.bitsandbobs.kradle") version "2.0.1"
}

group = "net.bitsandbobs.kradle"
version = "main-SNAPSHOT"

buildscript {
    dependencies {
        classpath(Catalog.BuildDependencies.jgit)
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(platform(Catalog.BuildDependencies.Platform.kotlin))
    implementation(Catalog.BuildDependencies.kotlinStdlib)
    implementation(Catalog.BuildDependencies.jgit)

    // Plugins
    implementation(Catalog.BuildDependencies.Plugins.kotlin)
    implementation(Catalog.BuildDependencies.Plugins.allOpen)
    implementation(Catalog.BuildDependencies.Plugins.kotlinSerialization)
    implementation(Catalog.BuildDependencies.Plugins.dokka)
    implementation(Catalog.BuildDependencies.Plugins.kotlinBenchmark)

    implementation(Catalog.BuildDependencies.Plugins.testLogger)
    implementation(Catalog.BuildDependencies.Plugins.shadow)
    implementation(Catalog.BuildDependencies.Plugins.jib)
    implementation(Catalog.BuildDependencies.Plugins.versions)
    implementation(Catalog.BuildDependencies.Plugins.detekt)
    implementation(Catalog.BuildDependencies.Plugins.ktlint)
    implementation(Catalog.BuildDependencies.Plugins.owaspDependencyCheck)

    implementation(Catalog.BuildDependencies.Plugins.spotbugs)

    // Testing
    testImplementation(Catalog.BuildDependencies.Test.kotlinTest)
    testImplementation(Catalog.BuildDependencies.Test.mockk)
    testImplementation(Catalog.BuildDependencies.Test.dockerJava)
    Catalog.BuildDependencies.Test.kotestBundle.forEach { testImplementation(it) }

    constraints {
        Catalog.Constraints.ids.forEach {
            api(it)
            implementation(it)
        }
    }
}

kradle {
    jvm {
        targetJvm("1.8")
        kotlin.enable()
        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withJunitJupiter()
            withJacoco()
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
            description = "Swiss army knife for Kotlin/JVM development"
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
