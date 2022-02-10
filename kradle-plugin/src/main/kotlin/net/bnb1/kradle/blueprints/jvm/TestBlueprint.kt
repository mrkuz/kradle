package net.bnb1.kradle.blueprints.jvm

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestLoggerPlugin
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.createTask
import net.bnb1.kradle.sourceSets
import net.bnb1.kradle.testImplementation
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

private const val RUN_TESTS_TASK = "runTests"

class TestBlueprint(project: Project) : Blueprint(project) {

    lateinit var testProperties: TestProperties
    lateinit var javaProperties: JavaProperties
    lateinit var withJunitJupiter: () -> Boolean

    override fun doApplyPlugins() {
        if (testProperties.prettyPrint) {
            project.apply(TestLoggerPlugin::class.java)
        }
    }

    // compat: Must be public we can create the tasks eagerly
    public override fun doCreateTasks() {
        // compat: Avoid duplicate creation on activate
        if (project.tasks.findByName(RUN_TESTS_TASK) == null) {
            project.createTask<Task>(RUN_TESTS_TASK, "Runs all tests") {
                dependsOn("test")
            }
        }

        val customTests = mutableListOf<String>()
        testProperties.withCustomTests.forEach {
            customTests.remove(it)
            customTests.add(it)
        }

        if (testProperties.withFunctionalTests) {
            customTests.remove("functional")
            customTests.add(0, "functional")
        }
        if (testProperties.withIntegrationTests) {
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

        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        val sourceSet = project.sourceSets.create(name)
        sourceSet.compileClasspath += mainSourceSet.output
        sourceSet.runtimeClasspath += mainSourceSet.output

        project.configurations.names
            .filter { it.startsWith("test") }
            .forEach {
                val configName = it.replace(Regex("^test"), name)
                project.configurations.getByName(configName) {
                    extendsFrom(project.configurations.getByName(it))
                }
            }

        project.createHelperTask<Test>(name, description) {
            testClassesDirs = sourceSet.output.classesDirs
            classpath = sourceSet.runtimeClasspath
        }

        project.tasks.getByName("check").dependsOn(name)
        project.tasks.getByName(RUN_TESTS_TASK).dependsOn(name)
    }

    override fun doAddDependencies() {
        project.dependencies {
            testProperties.useArchUnit?.let {
                if (withJunitJupiter()) {
                    testImplementation("${Catalog.Dependencies.Test.archUnitJunit5}:$it")
                } else {
                    testImplementation("${Catalog.Dependencies.Test.archUnit}:$it")
                }
            }
            testProperties.useTestcontainers?.let {
                testImplementation("${Catalog.Dependencies.Test.testcontainers}:$it")
                if (withJunitJupiter()) {
                    testImplementation("${Catalog.Dependencies.Test.testcontainersJunit5}:$it")
                }
            }
        }
    }

    override fun doConfigure() {
        var testEvents = setOf(TestLogEvent.SKIPPED, TestLogEvent.PASSED, TestLogEvent.FAILED)
        if (testProperties.showStandardStreams) {
            testEvents += setOf(TestLogEvent.STANDARD_OUT, TestLogEvent.STANDARD_ERROR)
        }

        project.tasks.withType<Test> {
            environment("KRADLE_PROJECT_DIR", project.projectDir.absolutePath)
            environment("KRADLE_PROJECT_ROOT_DIR", project.rootDir.absolutePath)
            if (javaProperties.previewFeatures) {
                jvmArgs = jvmArgs + "--enable-preview"
            }
            testLogging {
                events = testEvents
            }
            include("**/*Test.class")
            include("**/*Tests.class")
            include("**/*IT.class")
            include("**/*Spec.class")
        }

        if (testProperties.prettyPrint) {
            project.extensions.getByType(TestLoggerExtension::class.java).apply {
                showStandardStreams = testProperties.showStandardStreams
            }
        }
    }
}
