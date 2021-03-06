package net.bnb1.kradle.support.tasks

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
        if (!project.rootDir.resolve(".gitignore").exists()) {
            file.writeText(javaClass.getResource("/gitignore")!!.readText())
        }
    }
}
