package net.bnb1.kradle.config.dsl.general

import net.bnb1.kradle.blueprints.general.ScriptsProperties
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.dsl.ValueList
import net.bnb1.kradle.dsl.ValueSet
import net.bnb1.kradle.support.tasks.Prompt

class ScriptsDsl(private val properties: ScriptsProperties) {

    operator fun String.invoke(spec: Spec.() -> Unit) {
        val script = ScriptsProperties.Script(this)
        spec.invoke(Spec(script))
        properties.scripts.add(script)
    }

    class Spec(private val script: ScriptsProperties.Script) {

        val description = Optional<String>(null) { script.description = it }
        val dependsOn = ValueSet(script.dependsOn)
        val commands = ValueList(script.commands)
        val prompts = ValueList(script.prompts)

        fun prompt(key: String, text: String, default: String) {
            script.prompts.add(Prompt(key, text, default))
        }
    }
}
