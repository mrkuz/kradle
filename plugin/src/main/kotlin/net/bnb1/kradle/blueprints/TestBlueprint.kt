package net.bnb1.kradle.blueprints

import net.bnb1.kradle.*
import net.bnb1.kradle.plugins.NoOpPlugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

object TestBlueprint : PluginBlueprint<NoOpPlugin> {

    override fun configureEager(project: Project) {
        setUpTestTask(project, "integrationTest", "Runs the integration tests")
        setUpTestTask(project, "functionalTest", "Runs the functional tests")
    }

    override fun configure(project: Project, extension: KradleExtension) {
        val tests = extension.tests
        val useJunitJupiter = tests.junitJupiterVersion.isPresent
        if (useJunitJupiter) {
            project.dependencies {
                testImplementation("org.junit.jupiter:junit-jupiter-api:${tests.junitJupiterVersion.get()}")
                testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${tests.junitJupiterVersion.get()}")
            }
        }

        val useKotest = tests.kotestVersion.isPresent
        if (useKotest) {
            project.dependencies {
                testImplementation("io.kotest:kotest-assertions-core:${tests.kotestVersion.get()}")
                if (useJunitJupiter) {
                    testImplementation("io.kotest:kotest-runner-junit5:${tests.kotestVersion.get()}")
                } else {
                    testImplementation("io.kotest:kotest-runner-junit4:${tests.kotestVersion.get()}")
                }
            }
        }

        val useMockk = tests.mockkVersion.isPresent
        if (useMockk) {
            project.dependencies {
                testImplementation("io.mockk:mockk:${tests.mockkVersion.get()}")
            }
        }

        project.tasks.withType<Test> {
            if (useJunitJupiter) {
                useJUnitPlatform()
            }
            testLogging {
                showStandardStreams = true
                events = setOf(TestLogEvent.SKIPPED, TestLogEvent.PASSED, TestLogEvent.FAILED)
            }
            include("**/*Test.class")
            include("**/*Tests.class")
            include("**/*IT.class")
        }
    }

    @Suppress("DEPRECATION")
    private fun setUpTestTask(project: Project, name: String, description: String) {
        val javaConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
        val testSourceSet = javaConvention.sourceSets.getByName(SourceSet.TEST_SOURCE_SET_NAME);
        val sourceSet = javaConvention.sourceSets.create(name);
        sourceSet.compileClasspath += testSourceSet.compileClasspath
        sourceSet.runtimeClasspath += testSourceSet.runtimeClasspath

        project.configurations.names
            .filter { it.startsWith("test") }
            .forEach {
                val configName = it.replace(Regex("^test"), name)
                project.configurations.getByName(configName) {
                    extendsFrom(project.configurations.getByName(it))
                }
            }

        project.create(name, description, Test::class.java).apply {
            testClassesDirs = sourceSet.output.classesDirs
            classpath = sourceSet.runtimeClasspath
            mustRunAfter("test")
        }

        project.tasks.getByName("check").dependsOn(name)
    }
}
