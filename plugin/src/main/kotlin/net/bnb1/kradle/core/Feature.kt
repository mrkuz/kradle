package net.bnb1.kradle.core

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
open class Feature(val name: String, private val taskName: String? = null) {

    enum class State {
        INACTIVE, ACTIVATING, ACTIVATED
    }

    lateinit var tracer: Tracer

    private var enabled = AtomicBoolean(false)
    private var disabled = AtomicBoolean(false)
    private val state = AtomicReference(State.INACTIVE)

    private var conflicts: Feature = NoFeature
    private var requires: Feature = NoFeature
    private val after = mutableSetOf<Feature>()
    private val blueprints = mutableSetOf<Blueprint>()

    val defaultTaskName
        get() = taskName ?: name

    infix fun belongsTo(featureSet: FeatureSet) {
        featureSet += this
    }

    infix fun conflictsWith(feature: Feature) {
        failIfNotInactive()
        conflicts = feature
    }

    infix fun requires(feature: Feature) {
        failIfNotInactive()
        requires = feature
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

    private fun failIfNotInactive() {
        if (!isInactive) IllegalStateException("Configuration not allowed when activated")
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
        if (conflicts != NoFeature && conflicts.isEnabled) {
            throw GradleException(
                "You can only enable '${this::class.simpleName}'" +
                    " or '${conflicts::class.simpleName}' feature"
            )
        }

        if (requires != NoFeature && !requires.isEnabled) {
            throw GradleException("'${this::class.simpleName}' requires '${requires::class.simpleName}' feature")
        }

        if (!enabled.compareAndSet(false, true)) {
            return
        }
    }

    fun disable() {
        disabled.set(true)
    }

    fun shouldActivateAfter(): Set<Feature> {
        if (requires != NoFeature) {
            return setOf(requires) + after
        }
        return after
    }

    val isEnabled
        get() = enabled.get() && !disabled.get()

    val isInactive
        get() = state.get() == State.INACTIVE
}
