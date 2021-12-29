import groovy.text.SimpleTemplateEngine
import org.eclipse.jgit.api.Git
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileWriter
import java.util.*

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id(Catalog.Plugins.gradlePublish.id) version Catalog.Plugins.gradlePublish.version
    id(Catalog.Plugins.kotlinJvm.id) version Catalog.Plugins.kotlinJvm.version
    id("net.bitsandbobs.kradle") version "2.0.0"
}

group = "net.bitsandbobs.kradle"
version = "main-SNAPSHOT"

buildscript {
    dependencies {
        classpath(Catalog.Dependencies.jgit)
    }
}

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
    testImplementation(Catalog.Dependencies.Test.mockk)
    testImplementation(Catalog.Dependencies.Test.dockerJava)
    Catalog.Dependencies.Test.kotestBundle.forEach { testImplementation(it) }

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

tasks.register<Copy>("copyCatalog") {
    val output = project.buildDir.resolve("generatedSources/main/kotlin")
    outputs.files(output)
    from(project.rootDir.resolve("buildSrc/src/main/kotlin/Catalog.kt"))
    into(output)
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
