package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase
import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Preset are used to preconfigure Kradle for a specific use case.
 *
 * Only one preset can be active.
 */
open class Preset(private val extension: KradleExtensionBase, private val lock: AtomicBoolean) {

    private val activated = AtomicBoolean(false)

    fun activate(action: KradleExtensionBase.() -> Unit = {}) {
        if (!activated.compareAndSet(false, true)) {
            return
        }
        if (!lock.compareAndSet(false, true)) {
            throw GradleException("You can only use one preset")
        }
        doConfigure(extension)
        action(extension)
        doActivate(extension)
    }

    protected open fun doConfigure(extension: KradleExtensionBase) = Unit
    protected open fun doActivate(extension: KradleExtensionBase) = Unit
}
