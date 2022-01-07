package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.apply
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.implementation
import net.bnb1.kradle.testImplementation
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin

class KotlinBlueprint(project: Project) : Blueprint(project) {

    lateinit var kotlinProperties: KotlinProperties
    lateinit var jvmProperties: JvmProperties
    lateinit var applicationProperties: ApplicationProperties
    lateinit var lintProperties: LintProperties
    lateinit var codeAnalysisProperties: CodeAnalysisProperties
    lateinit var testProperties: TestProperties
    lateinit var detektProperties: DetektProperties
    lateinit var ktlintProperties: KtlintProperties
    lateinit var kotlinTestProperties: KotlinTestProperties

    override fun checkPreconditions() {
        if (project.extensions.findByType(KotlinJvmProjectExtension::class.java) == null) {
            throw GradleException("Kotlin JVM plugin has to be applied")
        }
    }

    override fun applyPlugins() {
        project.apply(SerializationGradleSubplugin::class.java)
    }

    override fun addExtraProperties() {
        project.extra["kotlinVersion"] = project.getKotlinPluginVersion()
    }

    override fun addDependencies() {
        project.dependencies {
            implementation(platform(Catalog.Dependencies.Platform.kotlin))
            implementation(Catalog.Dependencies.kotlinStdlib)
            implementation(Catalog.Dependencies.kotlinReflect)
            if (kotlinProperties.kotlinxCoroutinesVersion.hasValue) {
                implementation(
                    "${Catalog.Dependencies.kotlinCoroutines}:${kotlinProperties.kotlinxCoroutinesVersion.get()}"
                )
            }
            testImplementation(Catalog.Dependencies.Test.kotlinTest)
        }
    }

    override fun configure() {
        project.tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = if (jvmProperties.targetJvm.get() == "8") {
                    "1.8"
                } else {
                    jvmProperties.targetJvm.get()
                }
                freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xjsr305=strict")
            }
        }
    }
}
