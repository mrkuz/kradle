package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType

class JavaBlueprint(project: Project) : Blueprint(project) {

    lateinit var javaProperties: JavaProperties
    lateinit var jvmProperties: JvmProperties

    override fun doCheckPreconditions() {
        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        if (getJavaRelease() < 8) {
            throw GradleException("Minimum supported JVM version is 8")
        }

        if (javaExtension.toolchain.languageVersion.isPresent) {
            val target = Integer.parseInt(jvmProperties.targetJvm.get())
            val toolchain = javaExtension.toolchain.languageVersion.get().asInt()
            if (target > toolchain) {
                throw GradleException("'targetJvm' must be ≤ toolchain language version ($toolchain)")
            }
        }
    }

    override fun doApplyPlugins() {
        project.apply(JavaPlugin::class.java)
    }

    override fun doConfigure() {
        val release = getJavaRelease()
        project.tasks.withType<JavaCompile> {
            options.release.set(release)
        }

        if (javaProperties.previewFeatures.get()) {
            project.tasks.withType<JavaCompile> {
                options.compilerArgs.add("--enable-preview")
            }
        }
    }

    private fun getJavaRelease(): Int {
        val major = JavaVersion.toVersion(jvmProperties.targetJvm.get()).majorVersion
        return Integer.parseInt(major)
    }
}
