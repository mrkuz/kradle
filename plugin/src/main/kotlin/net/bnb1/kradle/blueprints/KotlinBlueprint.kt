package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.implementation
import net.bnb1.kradle.plugins.NoOpPlugin
import net.bnb1.kradle.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object KotlinBlueprint : PluginBlueprint<NoOpPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        project.extra["kotlinVersion"] = project.getKotlinPluginVersion()

        project.dependencies {
            implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
            implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${extension.kotlinxCoroutinesVersion.get()}")
            testImplementation("org.jetbrains.kotlin:kotlin-test")
        }

        project.tasks.withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
                jvmTarget = extension.targetJvm.get()
            }
        }
    }
}