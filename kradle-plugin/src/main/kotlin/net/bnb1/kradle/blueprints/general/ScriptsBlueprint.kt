package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createScriptTask
import org.gradle.api.GradleException
import org.gradle.api.Project

class ScriptsBlueprint(project: Project) : Blueprint(project) {

    lateinit var scriptsProperties: ScriptsProperties

    override fun doCreateTasks() {
        scriptsProperties.scripts.forEach { script ->
            if (script.description == null) {
                throw GradleException("Missing description for '${script.name}'")
            }
            if (script.commands == null) {
                throw GradleException("No commands for '${script.name}'")
            }
            project.createScriptTask(script.name, script.description!!) {
                commands.set(project.provider { script.commands!!.invoke() })
                prompts.set(script.prompts)
                inputListener.set(script.inputListener)
            }
        }
    }
}
