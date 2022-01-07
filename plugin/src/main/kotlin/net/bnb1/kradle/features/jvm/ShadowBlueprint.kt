package net.bnb1.kradle.features.jvm

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.named

private const val TASK_NAME = "uberJar"

class ShadowBlueprint(project: Project) : Blueprint(project) {

    lateinit var uberJarProperties: PackageUberJarProperties

    override fun createTasks() {
        val jar = project.tasks.named<Jar>("jar").get()
        val main = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        val classpath = project.configurations.getByName("runtimeClasspath")
        project.createTask<ShadowJar>(TASK_NAME, "Creates Uber-JAR") {
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

    override fun configure() {
        project.tasks.named<ShadowJar>(TASK_NAME).configure {
            if (uberJarProperties.minimize.get()) {
                minimize()
            }
        }
    }
}
