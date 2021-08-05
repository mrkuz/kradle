package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.plugins.NoOpPlugin
import net.bnb1.kradle.testImplementation
import net.bnb1.kradle.testRuntimeOnly
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named

object TestBlueprint : PluginBlueprint<NoOpPlugin> {

    override fun configure(project: Project) {
        project.afterEvaluate {
            val extension = project.extensions.getByType(KradleExtension::class.java)
            val useJunitJupiter = extension.junitJupiterVersion.isPresent
            val useKotest = extension.kotestVersion.isPresent

            if (useJunitJupiter) {
                project.dependencies {
                    testImplementation("org.junit.jupiter:junit-jupiter-api:${extension.junitJupiterVersion.get()}")
                    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${extension.junitJupiterVersion.get()}}")
                }
            }

            if (useKotest) {
                project.dependencies {
                    testImplementation("io.kotest:kotest-assertions-core:${extension.kotestVersion.get()}")
                    if (useJunitJupiter) {
                        testImplementation("io.kotest:kotest-runner-junit5:${extension.kotestVersion.get()}")
                    } else {
                        testImplementation("io.kotest:kotest-runner-junit4:${extension.kotestVersion.get()}")
                    }
                }
            }

            project.tasks.named<Test>("test").configure {
                if (useJunitJupiter) {
                    useJUnitPlatform()
                }
                testLogging {
                    events = setOf(TestLogEvent.SKIPPED, TestLogEvent.PASSED, TestLogEvent.FAILED)
                }
                include("**/*Test.class")
                include("**/*Tests.class")
                include("**/*IT.class")
            }
        }
    }
}
