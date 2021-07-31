package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.TaskBlueprint
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.dependencies

object TestBlueprint : TaskBlueprint<Test> {

    override fun configure(project: Project, task: Test): Test {

        val extension = project.extensions.getByType(KradleExtension::class.java)

        project.dependencies {
            add("testImplementation", "org.junit.jupiter:junit-jupiter-api:${extension.junitJupiterVersion.get()}")
            add("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:${extension.junitJupiterVersion.get()}}")
        }

        return task.apply {
            useJUnitPlatform()
            testLogging {
                events = setOf(TestLogEvent.SKIPPED, TestLogEvent.PASSED, TestLogEvent.FAILED)
            }
            include("**/*Test.class")
            include("**/*Tests.class")
            include("**/*IT.class")
        }
    }
}
