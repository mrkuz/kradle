package net.bnb1.kradle.features

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass

open class Feature {

    enum class State {
        INACTIVE, ACTIVATING, ACTIVATED
    }

    private var parent: KClass<out FeatureSet>? = null
    private var enabled = AtomicBoolean(false)
    private val state = AtomicReference<State>(State.INACTIVE)
    private val blueprints = CopyOnWriteArrayList<Blueprint>()
    private val listeners = CopyOnWriteArrayList<FeatureListener>()

    fun addBlueprint(blueprint: Blueprint) {
        if (blueprints.addIfAbsent(blueprint)) {
            if (state.get() == State.ACTIVATED) {
                blueprint.activate()
            }
        }
    }

    fun addListener(listener: FeatureListener) {
        if (listeners.addIfAbsent(listener)) {
            if (state.get() == State.ACTIVATED || state.get() == State.ACTIVATING) {
                listener.onFeatureActivate(this::class)
            }
        }
    }

    fun setParent(parent: KClass<out FeatureSet>) {
        this.parent = parent
    }

    fun isParent(parent: KClass<out FeatureSet>) = this.parent == parent

    fun activate() {
        if (!state.compareAndSet(State.INACTIVE, State.ACTIVATING)) {
            return
        }
        // println("Activate feature: ${this::class.simpleName}")
        blueprints.forEach { it.activate() }
        // Run again to activate blueprints that were added in the first run
        blueprints.forEach { it.activate() }
        listeners.forEach { it.onFeatureActivate(this::class) }
        state.set(State.ACTIVATED)
    }

    fun enable() {
        if (!enabled.compareAndSet(false, true)) {
            return
        }
        // println("Enable feature: ${this::class.simpleName}")
    }

    fun isEnabled() = enabled.get()
}
