package net.bnb1.kradle.support.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateLog4jConfigTask : DefaultTask() {

    private val file: File = project.projectDir.resolve("src/main/resources/log4j2.xml")

    init {
        outputs.upToDateWhen { file.exists() }
    }

    @TaskAction
    fun run() {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText(javaClass.getResource("/log4j2.xml")!!.readText())
        }
    }
}
