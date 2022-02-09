package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.dsl.Properties
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.support.tasks.InputListener
import net.bnb1.kradle.support.tasks.Prompt

class ScriptsProperties : Properties() {

    val scripts = mutableListOf<Script>()

    class Script() {
        val name = Value<String>(null, null)
        val description = Value<String>(null, null)

        var commands: (() -> String)? = null
        val prompts = mutableListOf<Prompt>()
        var inputListener: InputListener? = null
    }
}
