package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.core.Properties
import net.bnb1.kradle.support.tasks.InputListener
import net.bnb1.kradle.support.tasks.Prompt

class ScriptsProperties : Properties {

    val scripts = mutableListOf<Script>()

    class Script(val name: String) {
        var description: String? = null
        var commands: (() -> String)? = null

        val prompts = mutableListOf<Prompt>()
        var inputListener: InputListener? = null
    }
}
