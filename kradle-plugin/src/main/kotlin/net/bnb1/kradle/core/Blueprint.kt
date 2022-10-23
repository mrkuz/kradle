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
    private val disabledBy = mutableSetOf<Feature>()

    infix fun dependsOn(feature: Feature) {
        check(!activated.get()) { "Configuration not allowed when activated" }
        dependsOn += feature
    }

    infix fun disabledBy(feature: Feature) {
        check(!activated.get()) { "Configuration not allowed when activated" }
        disabledBy += feature
    }

    fun activate(tracer: Tracer) {
        tracer.branch {
            val missing = dependsOn
                .filter { !it.isEnabled }
                .map { it.name }
            val disabled = disabledBy
                .filter { it.isEnabled }
                .map { it.name }

            if (missing.isNotEmpty()) {
                trace("! ${this@Blueprint::class.simpleName} (B, missing features: ${missing.joinToString(", ")})")
            } else if (disabled.isNotEmpty()) {
                trace("! ${this@Blueprint::class.simpleName} (B, disabled by: ${missing.joinToString(", ")})")
            } else if (!activated.compareAndSet(false, true)) {
                trace("! ${this@Blueprint::class.simpleName} (B, already activated)")
            } else if (!shouldActivate()) {
                trace("! ${this@Blueprint::class.simpleName} (B, skipped)")
            } else {
                trace("${this@Blueprint::class.simpleName} (B)")

                doCheckPreconditions()
                doApplyPlugins()
                doAddExtraProperties()
                doCreateSourceSets()
                doCreateTasks()
                doCreateScriptTasks()
                doAddAliases()
                doAddDependencies()
                doConfigure()
            }
        }
    }

    val isActivate
        get() = activated.get()

    protected open fun shouldActivate() = true
    protected open fun doCheckPreconditions() = Unit
    protected open fun doApplyPlugins() = Unit
    protected open fun doAddExtraProperties() = Unit
    protected open fun doCreateSourceSets() = Unit
    protected open fun doCreateTasks() = Unit
    protected open fun doCreateScriptTasks() = Unit
    protected open fun doAddAliases() = Unit
    protected open fun doAddDependencies() = Unit
    protected open fun doConfigure() = Unit
}
