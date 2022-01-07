package net.bnb1.kradle.features

import net.bnb1.kradle.tracer
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

    // Configuration
    val features = mutableSetOf<Feature>()

    operator fun plusAssign(feature: Feature) {
        features += feature
    }

    operator fun plusAssign(features: Collection<Feature>) {
        this.features += features
    }

    fun activate() {
        if (!tryActivate()) {
            throw GradleException("'${this::class.simpleName}' was already activated")
        }
    }

    fun tryActivate(): Boolean {
        if (!activated.compareAndSet(false, true)) {
            return false
        }
        val visited = mutableSetOf<Feature>()
        project.tracer.trace("${this::class.simpleName} (FS)")
        features.asSequence()
            .filter { it.isEnabled }
            .filter { it.isInactive }
            .forEach { activateOrdered(visited, it) }
        return true
    }

    private fun activateOrdered(visited: MutableSet<Feature>, feature: Feature) {
        if (!visited.add(feature)) {
            throw IllegalStateException("Dependency loop detected")
        }
        project.tracer.branch {
            feature.shouldActivateAfter().asSequence()
                .filter { it.isEnabled }
                .filter { it.isInactive }
                .forEach { activateOrdered(visited, it) }
            project.tracer.trace("${feature::class.simpleName} (F)")
            feature.activate()
        }
    }
}
