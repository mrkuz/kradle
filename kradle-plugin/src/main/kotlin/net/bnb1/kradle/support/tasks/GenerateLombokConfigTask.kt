package net.bnb1.kradle.support.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateLombokConfigTask : DefaultTask() {

    private val file: File = project.projectDir.resolve("lombok.config")

    init {
        outputs.upToDateWhen { file.exists() }
    }

    @TaskAction
    fun run() {
        if (!project.projectDir.resolve("lombok.config").exists()) {
            file.writeText(javaClass.getResource("/lombok.config")!!.readText())
        }
    }
}
