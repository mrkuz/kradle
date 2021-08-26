package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

object JacocoBlueprint : PluginBlueprint<JacocoPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        project.tasks.named("test").configure {
            finalizedBy("jacocoTestReport")
        }

        project.configure<JacocoPluginExtension> {
            toolVersion = extension.jacocoVersion.get()
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