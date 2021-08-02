package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

object JavaBlueprint : PluginBlueprint<JavaPlugin> {

    override fun configure(project: Project) {
        project.afterEvaluate {
            val extension = project.extensions.getByType(KradleExtension::class.java)
            val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
            val javaVersion = JavaVersion.toVersion(extension.targetJvm.get())
            javaExtension.sourceCompatibility = javaVersion
            javaExtension.targetCompatibility = javaVersion
        }
    }
}