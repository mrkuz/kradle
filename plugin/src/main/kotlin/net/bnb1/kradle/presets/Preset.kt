package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase
import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Preset are used to preconfigure Kradle for a specific use case.
 *
 * Only one preset can be active.
 */
open class Preset(private val lock: AtomicBoolean) {

    private val activated = AtomicBoolean(false)

    fun configure(extension: KradleExtensionBase) {
        onConfigure(extension)
    }

    fun activate(extension: KradleExtensionBase) {
        if (!activated.compareAndSet(false, true)) {
            return
        }
        if (!lock.compareAndSet(false, true)) {
            throw GradleException("You can only use one preset")
        }
        onActivate(extension)
    }

    protected open fun onConfigure(extension: KradleExtensionBase) = Unit
    protected open fun onActivate(extension: KradleExtensionBase) = Unit
}
