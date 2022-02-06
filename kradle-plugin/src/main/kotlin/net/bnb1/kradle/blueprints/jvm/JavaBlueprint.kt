package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.annotationProcessor
import net.bnb1.kradle.apply
import net.bnb1.kradle.compileOnly
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import net.bnb1.kradle.implementation
import net.bnb1.kradle.support.tasks.GenerateLombokConfigTask
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

private const val MIN_JAVA_VERSION = 8

class JavaBlueprint(project: Project) : Blueprint(project) {

    lateinit var javaProperties: JavaProperties
    lateinit var jvmProperties: JvmProperties

    override fun doCheckPreconditions() {
        if (getJavaRelease() < MIN_JAVA_VERSION) {
            throw GradleException("Minimum supported JVM version is 8")
        }

        project.afterEvaluate {
            val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
            if (javaExtension.toolchain.languageVersion.isPresent) {
                val target = Integer.parseInt(jvmProperties.targetJvm.get())
                val toolchain = javaExtension.toolchain.languageVersion.get().asInt()
                if (target > toolchain) {
                    throw GradleException("'targetJvm' must be ≤ toolchain language version ($toolchain)")
                }
            }
        }
    }

    override fun doApplyPlugins() {
        project.apply(JavaPlugin::class.java)
    }

    override fun doCreateTasks() {
        if (javaProperties.withLombok.hasValue) {
            project.createTask<GenerateLombokConfigTask>("generateLombokConfig", "Generates lombok.config")
            project.tasks.getByName("compileJava").dependsOn("generateLombokConfig")
        }
    }

    override fun doAddDependencies() {
        if (javaProperties.withLombok.hasValue) {
            project.dependencies {
                implementation("${Catalog.Dependencies.lombok}:${javaProperties.withLombok.get()}")
                annotationProcessor("${Catalog.Dependencies.lombok}:${javaProperties.withLombok.get()}")
                compileOnly("${Catalog.Dependencies.Tools.findBugsAnnotations}:${Catalog.Versions.findBugs}")
            }
        }
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
