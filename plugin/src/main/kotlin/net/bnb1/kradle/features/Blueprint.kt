package net.bnb1.kradle.features

import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass

open class Blueprint(protected val project: Project) : FeatureListener {

    private val activated = AtomicBoolean(false)

    fun activate() {
        if (!activated.compareAndSet(false, true)) {
            return
        }
        project.logger.info("Activate blueprint: ${this::class.simpleName}")
        checkPreconditions()
        applyPlugins()
        registerBlueprints()
        createSourceSets()
        createTasks()
        addAliases()
        addExtraProperties()
        addDependencies()
        configure()
        onActivate()
    }

    protected open fun applyPlugins() = Unit

    protected open fun checkPreconditions() = Unit
    protected open fun registerBlueprints() = Unit
    protected open fun createSourceSets() = Unit
    protected open fun createTasks() = Unit
    protected open fun addAliases() = Unit
    protected open fun addExtraProperties() = Unit
    protected open fun addDependencies() = Unit
    protected open fun configure() = Unit
    protected open fun onActivate() = Unit

    override fun onFeatureActivate(feature: KClass<out Feature>) = Unit
}
