package net.bnb1.kradle.features.jvm

import com.adarshr.gradle.testlogger.TestLoggerPlugin
import net.bnb1.kradle.*
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class TestBlueprint(project: Project) : Blueprint(project) {

    override fun applyPlugins() {
        val properties = project.propertiesRegistry.get<TestProperties>()
        if (properties.prettyPrint.get()) {
            project.apply(TestLoggerPlugin::class.java)
        }
    }

    override fun registerBlueprints() {
        val properties = project.propertiesRegistry.get<TestProperties>()
        if (properties.jacocoVersion.hasValue) {
            project.featureRegistry.get<TestFeature>().addBlueprint(JacocoBlueprint(project))
        }
    }

    // compat: Must be public we can create the tasks eagerly
    public override fun createTasks() {
        val properties = project.propertiesRegistry.get<TestProperties>()
        if (properties.withIntegrationTests.get()) {
            createTask("integrationTest", "Runs the integration tests")
        }
        if (properties.withFunctionalTests.get()) {
            createTask("functionalTest", "Runs the functional tests")
            if (properties.withIntegrationTests.hasValue) {
                project.tasks.getByName("functionalTest").shouldRunAfter("integrationTest")
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun createTask(name: String, description: String) {
        // compat: Avoid duplicate creation on activate
        if (project.sourceSets.findByName(name) != null) {
            return
        }

        val testSourceSet = project.sourceSets.getByName(SourceSet.TEST_SOURCE_SET_NAME);
        val sourceSet = project.sourceSets.create(name)
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

    override fun addDependencies() {
        val properties = project.propertiesRegistry.get<TestProperties>()
        if (properties.junitJupiterVersion.hasValue) {
            project.dependencies {
                testImplementation("org.junit.jupiter:junit-jupiter-api:${properties.junitJupiterVersion.get()}")
                testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${properties.junitJupiterVersion.get()}")
            }
        }
    }

    override fun configure() {
        val properties = project.propertiesRegistry.get<TestProperties>()
        project.tasks.withType<Test> {
            if (properties.junitJupiterVersion.hasValue) {
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
}