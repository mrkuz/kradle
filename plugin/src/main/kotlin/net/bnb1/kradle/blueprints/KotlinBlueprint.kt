package net.bnb1.kradle.blueprints

import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object KotlinBlueprint : PluginBlueprint<KotlinPluginWrapper> {

    override fun configure(project: Project) {
        project.dependencies {
            add("implementation", platform("org.jetbrains.kotlin:kotlin-bom"))
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            add("testImplementation", "org.jetbrains.kotlin:kotlin-test")
        }

        project.extra["kotlinVersion"] = project.getKotlinPluginVersion()

        project.tasks.withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
                jvmTarget = "1.8"
            }
        }
    }
}