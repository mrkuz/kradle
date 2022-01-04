package net.bnb1.kradle.features

import java.util.concurrent.CopyOnWriteArrayList
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
 * disabled). This in turn activates assigned [blueprints][Blueprint] and notifies attached
 * [listeners][FeatureListener].
 */
open class Feature {

    enum class State {
        INACTIVE, ACTIVATING, ACTIVATED
    }

    private var parent: KClass<out FeatureSet>? = null
    private var enabled = AtomicBoolean(false)
    private var disabled = AtomicBoolean(false)
    private val state = AtomicReference(State.INACTIVE)
    private val after = CopyOnWriteArrayList<KClass<out Feature>>()
    private val blueprints = CopyOnWriteArrayList<Blueprint>()
    private val listeners = CopyOnWriteArrayList<FeatureListener>()

    fun after(vararg features: KClass<out Feature>) {
        for (feature in features) {
            if (this::class != feature) {
                after.addIfAbsent(feature)
            }
        }
    }

    fun shouldActivateAfter() = after.toList()

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
        if (!isEnabled) {
            return
        }
        if (!state.compareAndSet(State.INACTIVE, State.ACTIVATING)) {
            return
        }

        blueprints.forEach { it.activate() }
        // Run again to activate blueprints that were added in the first run
        blueprints
            .filter { !it.isActivated }
            .forEach { it.activate() }
        listeners.forEach { it.onFeatureActivate(this::class) }
        state.set(State.ACTIVATED)
    }

    fun enable() {
        if (!enabled.compareAndSet(false, true)) {
            return
        }
    }

    fun disable() {
        disabled.set(true)
    }

    val isEnabled
        get() = enabled.get() && !disabled.get()

    val isInactive
        get() = state.get() == State.INACTIVE
}
