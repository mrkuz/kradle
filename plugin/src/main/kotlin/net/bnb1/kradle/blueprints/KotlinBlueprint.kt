package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.annotations.Invasive
import net.bnb1.kradle.implementation
import net.bnb1.kradle.plugins.NoOpPlugin
import net.bnb1.kradle.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Invasive
object KotlinBlueprint : PluginBlueprint<NoOpPlugin> {

    override fun configureEager(project: Project) {
        project.extra["kotlinVersion"] = project.getKotlinPluginVersion()
    }

    override fun configure(project: Project, extension: KradleExtension) {
        configureDependencies(project, extension)

        project.tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = extension.targetJvm.get()
                freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
            }
        }
    }

    private fun configureDependencies(project: Project, extension: KradleExtension) {
        project.dependencies {
            implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
            implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            implementation("org.jetbrains.kotlin:kotlin-reflect")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${extension.kotlinxCoroutinesVersion.get()}")
            testImplementation("org.jetbrains.kotlin:kotlin-test")
        }
    }
}
