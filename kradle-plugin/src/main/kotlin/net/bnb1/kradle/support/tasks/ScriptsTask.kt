package net.bnb1.kradle.support.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.internal.tasks.userinput.UserInputHandler
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.support.serviceOf
import org.gradle.process.ExecOperations
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
    val inputListener = project.objects.property(InputListener::class.java)

    @get:Internal
    val commands = project.objects.property(String::class.java)

    @TaskAction
    fun run() {
        if (prompts.get().isNotEmpty() && !inputListener.isPresent) {
            throw GradleException("Can't use prompts without input listener")
        }

        val userInputHandler = project.serviceOf<UserInputHandler>()
        prompts.get().forEach {
            val value = userInputHandler.askQuestion(it.text, it.default)
            inputListener.get().onInput(it.key, value)
        }

        commands.get().lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach {
                execOperations.exec {
                    if (echo) {
                        commandLine(("echo $it").split(" "))
                    } else {
                        commandLine(it.split(" "))
                    }
                }
            }
    }
}

data class Prompt(val key: String, val text: String, val default: String)

fun interface InputListener {
    fun onInput(key: String, value: String)
}
