package net.bnb1.kradle.core

import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Blueprints are used to implement logic for [features][Feature].
 *
 * They describe which plugins to apply and how to configure them.
 */
open class Blueprint(protected val project: Project) {

    private val activated = AtomicBoolean(false)
    private val dependsOn = mutableSetOf<Feature>()

    infix fun dependsOn(feature: Feature) {
        if (activated.get()) throw IllegalStateException("Configuration not allowed when activated")
        dependsOn += feature
    }

    fun activate(tracer: Tracer) {
        if (dependsOn.any { !it.isEnabled }) {
            return
        }

        if (!activated.compareAndSet(false, true)) {
            return
        }

        if (!shouldActivate()) {
            tracer.branch {
                trace("${this@Blueprint::class.simpleName} (B, skipped)")
            }
            return
        }

        tracer.branch {
            trace("${this@Blueprint::class.simpleName} (B)")

            doCheckPreconditions()
            doApplyPlugins()
            doCreateSourceSets()
            doCreateTasks()
            doAddAliases()
            doAddExtraProperties()
            doAddDependencies()
            doConfigure()
        }
    }

    val isActivated
        get() = activated.get()

    protected open fun shouldActivate() = true
    protected open fun doCheckPreconditions() = Unit
    protected open fun doApplyPlugins() = Unit
    protected open fun doCreateSourceSets() = Unit
    protected open fun doCreateTasks() = Unit
    protected open fun doAddAliases() = Unit
    protected open fun doAddExtraProperties() = Unit
    protected open fun doAddDependencies() = Unit
    protected open fun doConfigure() = Unit
}
