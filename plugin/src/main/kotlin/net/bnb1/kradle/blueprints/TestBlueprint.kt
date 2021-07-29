package net.bnb1.kradle.blueprints

import net.bnb1.kradle.TaskBlueprint
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.dependencies

object TestBlueprint : TaskBlueprint<Test> {

    override fun configure(project: Project, task: Test): Test {
        project.dependencies {
            add("testImplementation", "org.junit.jupiter:junit-jupiter-api:5.7.2")
            add("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:5.7.2")
        }

        return task.apply {
            useJUnitPlatform()
            testLogging {
                events = setOf(TestLogEvent.SKIPPED, TestLogEvent.PASSED, TestLogEvent.FAILED)
            }
            includes.add("**/*Tests*.class")
        }
    }
}