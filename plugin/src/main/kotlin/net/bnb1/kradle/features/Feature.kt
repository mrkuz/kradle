package net.bnb1.kradle.features

import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * A Kradle feature.
 *
 * Feature classes don't implement the logic. This is the job of [blueprints][Blueprint]. A feature can have one or more
 * [blueprints][Blueprint].
 *
 * Every feature has a parent [feature set][FeatureSet]. If the set is activated, the feature is also activated (unless
 * disabled). This in turn activates assigned [blueprints][Blueprint].
 */
open class Feature() {

    enum class State {
        INACTIVE, ACTIVATING, ACTIVATED
    }

    lateinit var tracer: Tracer

    private var enabled = AtomicBoolean(false)
    private var disabled = AtomicBoolean(false)
    private val state = AtomicReference(State.INACTIVE)

    // Configuration
    var conflictsWith: Feature? = null
    var requires: Feature? = null
    val after = mutableSetOf<Feature>()
    private val blueprints = mutableSetOf<Blueprint>()

    operator fun plusAssign(blueprint: Blueprint) {
        addBlueprint(blueprint)
    }

    operator fun plusAssign(blueprints: Collection<Blueprint>) {
        blueprints.forEach { addBlueprint(it) }
    }

    fun addBlueprint(blueprint: Blueprint) {
        if (blueprints.add(blueprint)) {
            if (state.get() == State.ACTIVATED) {
                blueprint.activate(tracer)
            }
        }
    }

    fun activate(tracer: Tracer) {
        if (!isEnabled) {
            return
        }
        if (!state.compareAndSet(State.INACTIVE, State.ACTIVATING)) {
            return
        }

        this.tracer = tracer
        blueprints.forEach { it.activate(tracer) }
        state.set(State.ACTIVATED)
    }

    fun enable() {
        if (conflictsWith != null) {
            if (conflictsWith!!.isEnabled) {
                throw GradleException(
                    "You can only enable '${this::class.simpleName}'" +
                        " or '${conflictsWith!!::class.simpleName}' feature"
                )
            }
        }
        if (requires != null) {
            if (!requires!!.isEnabled) {
                throw GradleException("'${this::class.simpleName}' requires '${requires!!::class.simpleName}' feature")
            }
        }

        if (!enabled.compareAndSet(false, true)) {
            return
        }
    }

    fun disable() {
        disabled.set(true)
    }

    fun shouldActivateAfter(): Set<Feature> {
        if (requires != null) {
            return setOf(requires!!) + after
        }
        return after
    }

    val isEnabled
        get() = enabled.get() && !disabled.get()

    val isInactive
        get() = state.get() == State.INACTIVE
}
