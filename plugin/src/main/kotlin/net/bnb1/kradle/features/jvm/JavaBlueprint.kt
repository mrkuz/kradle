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

class JavaBlueprint(project: Project) : Blueprint(project) {

    override fun checkPreconditions() {
        val properties = project.propertiesRegistry.get<JvmProperties>()
        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)

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
        val properties = project.propertiesRegistry.get<JvmProperties>()
        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)

        if (!javaExtension.toolchain.languageVersion.isPresent) {
            val javaVersion = JavaVersion.toVersion(properties.targetJvm.get())
            javaExtension.sourceCompatibility = javaVersion
            javaExtension.targetCompatibility = javaVersion
        }
    }
}
