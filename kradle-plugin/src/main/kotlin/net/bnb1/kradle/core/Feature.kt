package net.bnb1.kradle.core

import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass

/**
 * A Kradle feature.
 *
 * Feature classes don't implement the logic. This is the job of [blueprints][Blueprint]. A feature can have one or more
 * [blueprints][Blueprint].
 *
 * Every feature has a parent [feature set][FeatureSet]. If the set is activated, the feature is also activated (unless
 * disabled). This in turn activates assigned [blueprints][Blueprint].
 */
class Feature(val name: String, private val taskName: String? = null) {

    enum class State {
        INACTIVE, ACTIVATING, ACTIVATED
    }

    lateinit var tracer: Tracer

    private val enabled = AtomicBoolean(false)
    private val state = AtomicReference(State.INACTIVE)

    private var conflicts = mutableSetOf<Feature>()
    private var requires = mutableSetOf<Feature>()
    private val after = mutableSetOf<Feature>()
    private val blueprints = mutableSetOf<Blueprint>()

    val defaultTaskName
        get() = taskName ?: "NONE"

    infix fun belongsTo(featureSet: FeatureSet) {
        featureSet += this
    }

    infix fun conflictsWith(feature: Feature) {
        failIfNotInactive()
        feature.conflicts += this
        conflicts += feature
    }

    infix fun requires(feature: Feature) {
        failIfNotInactive()
        requires += feature
    }

    infix fun activatesAfter(feature: Feature) {
        failIfNotInactive()
        after += feature
    }

    operator fun plusAssign(blueprint: Blueprint) {
        failIfNotInactive()
        blueprints += blueprint
    }

    operator fun plusAssign(blueprints: Collection<Blueprint>) {
        failIfNotInactive()
        this.blueprints += blueprints
    }

    fun hasBlueprint(blueprint: KClass<out Blueprint>) = blueprints.any { it::class == blueprint }

    private fun failIfNotInactive() {
        if (!isInactive) throw IllegalStateException("Configuration not allowed when activated")
    }

    fun enable() {
        enabled.set(true)
    }

    fun disable() {
        enabled.set(false)
    }

    val isEnabled
        get() = enabled.get()

    fun activate(tracer: Tracer) {
        if (!isEnabled) {
            return
        }
        if (!state.compareAndSet(State.INACTIVE, State.ACTIVATING)) {
            return
        }

        conflicts.find { it.isEnabled }?.let {
            throw GradleException("You can only enable '$name'" + " or '${it.name}' feature")
        }

        requires.find { !it.isEnabled }?.let {
            throw GradleException("'$name' requires '${it.name}' feature")
        }

        this.tracer = tracer
        blueprints.forEach { it.activate(tracer) }
        state.set(State.ACTIVATED)
    }

    fun shouldActivateAfter(): Set<Feature> {
        return requires + after
    }

    val isActive
        get() = state.get() == State.ACTIVATED

    val isInactive
        get() = state.get() == State.INACTIVE
}
