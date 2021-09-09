package net.bnb1.kradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateGitignoreTask : DefaultTask() {

    private val file: File = project.rootDir.resolve(".gitignore")

    init {
        outputs.upToDateWhen { file.exists() }
    }

    @TaskAction
    fun run() {
        file.writeText(javaClass.getResource("/gitignore")!!.readText())
    }
}