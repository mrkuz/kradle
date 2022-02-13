package net.bnb1.kradle.support.tasks

import groovy.text.SimpleTemplateEngine
import org.gradle.api.DefaultTask
import org.gradle.api.internal.tasks.userinput.UserInputHandler
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.support.serviceOf
import org.gradle.process.ExecOperations
import java.io.StringWriter
import javax.inject.Inject

open class ScriptsTask @Inject constructor(private val execOperations: ExecOperations) : DefaultTask() {

    init {
        outputs.upToDateWhen { false }
    }

    @get:Internal
    @Option(option = "echo", description = "Echo commands instead of executing them")
    var echo: Boolean = false

    @get:Internal
    val prompts = project.objects.listProperty(Prompt::class.java)

    @get:Internal
    val commands = project.objects.listProperty(String::class.java)

    @TaskAction
    fun run() {
        val userInputHandler = project.serviceOf<UserInputHandler>()
        val inputs = mutableMapOf<String, String>()
        prompts.get().forEach {
            val value = userInputHandler.askQuestion(it.text, it.default)
            inputs[it.key] = value
        }

        val engine = SimpleTemplateEngine()

        commands.get().asSequence()
            .flatMap { it.lines() }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach {
                val template = engine.createTemplate(it.replace("$#", "$"))
                val prepared = template.make(mutableMapOf("inputs" to inputs))
                val writer = StringWriter()
                prepared.writeTo(writer)
                val command = writer.toString()

                execOperations.exec {
                    if (echo) {
                        commandLine(("echo $command").split(" "))
                    } else {
                        commandLine(command.split(" "))
                    }
                }
            }
    }
}

data class Prompt(val key: String, val text: String, val default: String)
