package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.blueprints.general.ScriptsProperties
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.support.tasks.InputListener
import net.bnb1.kradle.support.tasks.Prompt

class ScriptsDsl(private val properties: AllProperties) {

    operator fun String.invoke(spec: Spec.() -> Unit) {
        val script = ScriptsProperties.Script()
        spec.invoke(Spec(script, this))
        properties.scripts.scripts.add(script)
    }

    class Spec(private val script: ScriptsProperties.Script, name: String) {

        val description = script.description
        val inputs = mutableMapOf<String, String>()

        init {
            script.name.set(name)
            script.inputListener = object : InputListener {
                override fun onInput(key: String, value: String) {
                    inputs[key] = value
                }
            }
        }

        fun commands(provider: () -> String) {
            script.commands = provider
        }

        fun prompt(key: String, text: String, default: String) {
            script.prompts.add(Prompt(key, text, default))
        }
    }
}
