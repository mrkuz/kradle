package net.bnb1.kradle.blueprints

import net.bnb1.kradle.*
import net.bnb1.kradle.annotations.Invasive
import net.bnb1.kradle.plugins.NoOpPlugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

@Invasive
object TestBlueprint : PluginBlueprint<NoOpPlugin> {

    override fun configureEager(project: Project) {
        createTask(project, "integrationTest", "Runs the integration tests")
        createTask(project, "functionalTest", "Runs the functional tests")
    }

    override fun configure(project: Project, extension: KradleExtension) {
        configureDependencies(project, extension)

        val useJunitJupiter = extension.tests.junitJupiterVersion.isPresent
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
    private fun createTask(project: Project, name: String, description: String) {
        val testSourceSet = project.sourceSets.getByName(SourceSet.TEST_SOURCE_SET_NAME);
        val sourceSet = project.sourceSets.create(name);
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

        project.create<Test>(name, description) {
            testClassesDirs = sourceSet.output.classesDirs
            classpath = sourceSet.runtimeClasspath
            mustRunAfter("test")
        }

        project.tasks.getByName("check").dependsOn(name)
    }

    private fun configureDependencies(project: Project, extension: KradleExtension) {
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
    }
}
