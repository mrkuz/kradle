package net.bnb1.kradle.core

import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Preset are used to preconfigure Kradle for a specific use case.
 *
 * Only one preset can be active.
 */
open class Preset<T : Any>(private val lock: AtomicBoolean) {

    private val activated = AtomicBoolean(false)

    fun configure(target: T) {
        doConfigure(target)
    }

    fun activate(target: T) {
        if (!activated.compareAndSet(false, true)) {
            return
        }
        if (!lock.compareAndSet(false, true)) {
            throw GradleException("You can only use one preset")
        }
        doActivate(target)
    }

    protected open fun doConfigure(target: T) = Unit
    protected open fun doActivate(target: T) = Unit
}
