package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.*
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.general.BootstrapFeature
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin

class KotlinBlueprint(project: Project) : Blueprint(project) {

    override fun checkPreconditions() {
        if (project.extensions.findByType(KotlinJvmProjectExtension::class.java) == null) {
            throw GradleException("Kotlin JVM plugin has to be applied")
        }
    }

    override fun registerBlueprints() {
        with(project.featureRegistry) {
            get<BootstrapFeature>().addBlueprint(KotlinBootstrapBlueprint(project))
            get<CodeAnalysisFeature>().addBlueprint(DetektBlueprint(project))
            get<LintFeature>().addBlueprint(KtlintBlueprint(project))
            get<TestFeature>().addBlueprint(KotlinTestBlueprint(project))
        }
    }

    override fun applyPlugins() {
        project.apply(AllOpenGradleSubplugin::class.java)
        project.apply(SerializationGradleSubplugin::class.java)
    }

    override fun addExtraProperties() {
        project.extra["kotlinVersion"] = project.getKotlinPluginVersion()
    }

    override fun addDependencies() {
        val properties = project.propertiesRegistry.get<KotlinProperties>()
        project.dependencies {
            implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
            implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            implementation("org.jetbrains.kotlin:kotlin-reflect")
            if (properties.kotlinxCoroutinesVersion.hasValue) {
                implementation(
                    "org.jetbrains.kotlinx:kotlinx-coroutines-core:${properties.kotlinxCoroutinesVersion.get()}"
                )
            }
            testImplementation("org.jetbrains.kotlin:kotlin-test")
        }
    }

    override fun configure() {
        val properties = project.propertiesRegistry.get<JvmProperties>()
        project.tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = properties.targetJvm.get()
                freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xjsr305=strict")
            }
        }
    }
}
