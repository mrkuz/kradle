package net.bnb1.kradle.core

import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A feature set is used to group [features][Feature].
 *
 * If the set is activated, the assigned and enabled features are also activated.
 */
class FeatureSet(val name: String) {

    private val activated = AtomicBoolean(false)

    private val features = mutableSetOf<Feature>()

    operator fun plusAssign(feature: Feature) {
        failIfActive()
        features += feature
    }

    operator fun plusAssign(features: Collection<Feature>) {
        failIfActive()
        this.features += features
    }

    fun hasFeature(feature: Feature) = features.contains(feature)

    private fun failIfActive() {
        if (activated.get()) throw IllegalStateException("Configuration not allowed when activated")
    }

    fun activate(tracer: Tracer) {
        if (!tryActivate(tracer)) {
            throw GradleException("'${this::class.simpleName}' was already activated")
        }
    }

    fun tryActivate(tracer: Tracer): Boolean {
        if (!activated.compareAndSet(false, true)) {
            return false
        }
        val visited = mutableSetOf<Feature>()
        tracer.trace("$name (FS)")
        features.asSequence()
            .filter { it.isEnabled }
            .filter { it.isInactive }
            .forEach { activateOrdered(tracer, visited, it) }
        return true
    }

    private fun activateOrdered(tracer: Tracer, visited: MutableSet<Feature>, feature: Feature) {
        if (!visited.add(feature)) {
            throw IllegalStateException("Dependency loop detected")
        }
        tracer.branch {
            feature.shouldActivateAfter().asSequence()
                .filter { it.isEnabled }
                .filter { it.isInactive }
                .forEach { activateOrdered(tracer, visited, it) }
            tracer.trace("${feature.name} (F)")
            feature.activate(tracer)
        }
    }
}
