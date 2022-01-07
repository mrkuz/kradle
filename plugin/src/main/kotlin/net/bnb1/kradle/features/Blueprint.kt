package net.bnb1.kradle.features

import net.bnb1.kradle.tracer
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Blueprints are used to implement logic for [features][Feature].
 *
 * They describe which plugins to apply and how to configure them.
 */
open class Blueprint(protected val project: Project) {

    private val activated = AtomicBoolean(false)

    // Configuration
    val dependsOn = mutableSetOf<Feature>()

    fun activate() {
        if (dependsOn.any { !it.isEnabled }) {
            return
        }

        if (!activated.compareAndSet(false, true)) {
            return
        }

        if (!shouldActivate()) {
            project.tracer.branch {
                trace("${this@Blueprint::class.simpleName} (B, skipped)")
            }
            return
        }

        project.tracer.branch {
            trace("${this@Blueprint::class.simpleName} (B)")

            checkPreconditions()
            applyPlugins()
            createSourceSets()
            createTasks()
            addAliases()
            addExtraProperties()
            addDependencies()
            configure()
        }
    }

    val isActivated
        get() = activated.get()

    protected open fun shouldActivate() = true
    protected open fun checkPreconditions() = Unit
    protected open fun applyPlugins() = Unit
    protected open fun createSourceSets() = Unit
    protected open fun createTasks() = Unit
    protected open fun addAliases() = Unit
    protected open fun addExtraProperties() = Unit
    protected open fun addDependencies() = Unit
    protected open fun configure() = Unit
}
