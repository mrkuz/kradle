package net.bnb1.kradle.features.jvm

import com.adarshr.gradle.testlogger.TestLoggerPlugin
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.apply
import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.sourceSets
import net.bnb1.kradle.testImplementation
import net.bnb1.kradle.testRuntimeOnly
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

    // compat: Must be public we can create the tasks eagerly
    public override fun createTasks() {
        val properties = project.propertiesRegistry.get<TestProperties>()
        val customTests = mutableListOf<String>()
        customTests.addAll(properties.customTests.get())

        if (properties.withFunctionalTests.get()) {
            customTests.remove("functional")
            customTests.add(0, "functional")
        }
        if (properties.withIntegrationTests.get()) {
            customTests.remove("integration")
            customTests.add(0, "integration")
        }

        for (i in 0 until customTests.size) {
            val name = customTests[i]
            createTask("${name}Test", "Runs the $name tests")
            if (i == 0) {
                project.tasks.findByName("${name}Test")!!.shouldRunAfter("test")
            } else {
                val prevName = customTests[i - 1]
                project.tasks.findByName("${name}Test")!!.shouldRunAfter("${prevName}Test")
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun createTask(name: String, description: String) {
        // compat: Avoid duplicate creation on activate
        if (project.sourceSets.findByName(name) != null) {
            return
        }

        val testSourceSet = project.sourceSets.getByName(SourceSet.TEST_SOURCE_SET_NAME)
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

        project.createTask<Test>(name, description) {
            testClassesDirs = sourceSet.output.classesDirs
            classpath = sourceSet.runtimeClasspath
            mustRunAfter("test")
        }

        project.tasks.getByName("check").dependsOn(name)
    }

    override fun addDependencies() {
        val properties = project.propertiesRegistry.get<TestProperties>()
        if (properties.withJunitJupiter.hasValue) {
            project.dependencies {
                testImplementation("${Catalog.Dependencies.Test.junitApi}:${properties.withJunitJupiter.get()}")
                testRuntimeOnly("${Catalog.Dependencies.Test.junitEngine}:${properties.withJunitJupiter.get()}")
            }
        }
    }

    override fun configure() {
        val testProperties = project.propertiesRegistry.get<TestProperties>()
        val javaProperties = project.propertiesRegistry.get<JavaProperties>()

        project.tasks.withType<Test> {
            if (testProperties.withJunitJupiter.hasValue) {
                useJUnitPlatform()
            }
            if (javaProperties.previewFeatures.get()) {
                jvmArgs = jvmArgs + "--enable-preview"
            }
            testLogging {
                showStandardStreams = true
                events = setOf(TestLogEvent.SKIPPED, TestLogEvent.PASSED, TestLogEvent.FAILED)
            }
            include("**/*Test.class")
            include("**/*Tests.class")
            include("**/*IT.class")
            include("**/*Spec.class")
        }
    }
}
