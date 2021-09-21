package net.bnb1.kradle.blueprints

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.create
import net.bnb1.kradle.plugins.NoOpPlugin
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.named

object ShadowBlueprint : PluginBlueprint<NoOpPlugin> {

    private const val TASK_NAME = "uberJar"

    override fun configureEager(project: Project) {
        val jar = project.tasks.named<Jar>("jar").get()
        val main = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        val classpath = project.configurations.getByName("runtimeClasspath")
        project.create<ShadowJar>(TASK_NAME, "Creates Uber-JAR") {
            archiveClassifier.set("uber")
            manifest.inheritFrom(jar.manifest)
            from(main.output)
            configurations = listOf(classpath)
            exclude(
                "META-INF/INDEX.LIST",
                "META-INF/*.SF",
                "META-INF/*.DSA",
                "META-INF/*.RSA",
                "module-info.class"
            )
        }
    }

    override fun configure(project: Project, extension: KradleExtension) {
        project.tasks.named<ShadowJar>(TASK_NAME).configure {
            // Remove unused dependencies
            minimize()
        }
    }
}
