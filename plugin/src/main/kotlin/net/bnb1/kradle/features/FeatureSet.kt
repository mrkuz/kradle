package net.bnb1.kradle.features

import net.bnb1.kradle.support.Tracer
import org.gradle.api.GradleException
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A feature set is used to group [features][Feature].
 *
 * If the set is activated, the assigned and enabled features are also activated.
 */
open class FeatureSet(private val project: Project) {

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
        tracer.trace("${this::class.simpleName} (FS)")
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
            tracer.trace("${feature::class.simpleName} (F)")
            feature.activate(tracer)
        }
    }
}
