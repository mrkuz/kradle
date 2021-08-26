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

        project.tasks.named<Test>("test").configure {
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
}
