package net.bnb1.kradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

open class GenerateCheckstyleConfigTask : DefaultTask() {

    @Internal
    val outputFile = project.objects.fileProperty()

    init {
        outputs.upToDateWhen { outputFile.get().asFile.exists() }
    }

    @TaskAction
    fun run() {
        outputFile.get().asFile.writeText(javaClass.getResource("/checkstyle.xml")!!.readText())
    }
}