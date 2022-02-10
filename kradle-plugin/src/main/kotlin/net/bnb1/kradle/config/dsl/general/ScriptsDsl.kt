package net.bnb1.kradle.config.dsl.general

import net.bnb1.kradle.blueprints.general.ScriptsProperties
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.support.tasks.InputListener
import net.bnb1.kradle.support.tasks.Prompt

class ScriptsDsl(private val properties: AllProperties) {

    operator fun String.invoke(spec: Spec.() -> Unit) {
        val script = ScriptsProperties.Script(this)
        spec.invoke(Spec(script))
        properties.scripts.scripts.add(script)
    }

    class Spec(private val script: ScriptsProperties.Script) {

        val description = Optional<String>(null) { script.description = it }
        val inputs = mutableMapOf<String, String>()

        init {
            script.inputListener = InputListener { key, value -> inputs[key] = value }
        }

        fun commands(provider: () -> String) {
            script.commands = provider
        }

        fun prompt(key: String, text: String, default: String) {
            script.prompts.add(Prompt(key, text, default))
        }
    }
}
