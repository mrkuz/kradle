package net.bnb1.kradle.blueprints

import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

object JacocoBlueprint : PluginBlueprint<JacocoPlugin> {

    override fun configure(project: Project) {
        project.tasks.named("test").configure {
            finalizedBy("jacocoTestReport")
        }
        project.tasks.named<JacocoReport>("jacocoTestReport").configure {
            dependsOn("test")
            reports {
                csv.required.set(false)
                xml.required.set(false)
                html.outputLocation.set(project.buildDir.resolve("reports/jacoco/"))
            }
        }
    }
}