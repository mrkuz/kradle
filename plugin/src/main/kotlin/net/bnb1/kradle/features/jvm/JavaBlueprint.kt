package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.general.BootstrapFeature
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType

class JavaBlueprint(project: Project) : Blueprint(project) {

    override fun checkPreconditions() {
        val properties = project.propertiesRegistry.get<JvmProperties>()
        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        if (getJavaRelease() < 8) {
            throw GradleException("Minimum supported JVM version is 8")
        }

        if (javaExtension.toolchain.languageVersion.isPresent) {
            val target = Integer.parseInt(properties.targetJvm.get())
            val toolchain = javaExtension.toolchain.languageVersion.get().asInt()
            if (target > toolchain) {
                throw GradleException("'targetJvm' must be ≤ toolchain language version ($toolchain)")
            }
        }
    }

    override fun applyPlugins() {
        project.apply(JavaPlugin::class.java)
    }

    override fun registerBlueprints() {
        with(project.featureRegistry) {
            if (get<JavaFeature>().isEnabled) {
                get<BootstrapFeature>().addBlueprint(JavaBootstrapBlueprint(project))
                get<CodeAnalysisFeature>().addBlueprint(PmdBlueprint(project))
                get<CodeAnalysisFeature>().addBlueprint(SpotBugsBlueprint(project))
                get<LintFeature>().addBlueprint(CheckstyleBlueprint(project))
            }
        }
    }

    override fun configure() {
        val release = getJavaRelease()
        project.tasks.withType<JavaCompile> {
            options.release.set(release)
        }

        val javaProperties = project.propertiesRegistry.get<JavaProperties>()
        if (javaProperties.previewFeatures.get()) {
            project.tasks.withType<JavaCompile> {
                options.compilerArgs.add("--enable-preview")
            }
        }
    }

    private fun getJavaRelease(): Int {
        val jvmProperties = project.propertiesRegistry.get<JvmProperties>()
        val major = JavaVersion.toVersion(jvmProperties.targetJvm.get()).majorVersion
        return Integer.parseInt(major)
    }
}
