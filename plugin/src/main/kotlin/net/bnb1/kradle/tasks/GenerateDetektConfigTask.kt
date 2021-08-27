package net.bnb1.kradle.tasks

import net.bnb1.kradle.KradleExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateDetektConfigTask : DefaultTask() {

    private val file: File

    init {
        val extension = project.extensions.getByType(KradleExtension::class.java)
        file = project.rootDir.resolve(extension.detektConfigFile.get())
        outputs.upToDateWhen { file.exists() }
    }

    @TaskAction
    fun run() {
        file.writeText(javaClass.getResource("/detekt-config.yml")!!.readText())
    }
}