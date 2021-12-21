package net.bnb1.kradle.tasks

import net.bnb1.kradle.features.jvm.KotlinCodeAnalysisProperties
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateDetektConfigTask : DefaultTask() {

    private val file: File

    init {
        // TODO: Replace with task property
        val properties = project.propertiesRegistry.get<KotlinCodeAnalysisProperties>()
        file = project.rootDir.resolve(properties.detektConfigFile.get())
        outputs.upToDateWhen { file.exists() }
    }

    @TaskAction
    fun run() {
        file.writeText(javaClass.getResource("/detekt-config.yml")!!.readText())
    }
}