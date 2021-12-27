package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.presetRegistry
import org.gradle.api.GradleException
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Preset are used to preconfigure Kradle for a specific use case.
 *
 * Only one preset can be active.
 */
open class Preset(private val project: Project) {

    private val activated = AtomicBoolean(false)

    fun configure(extension: KradleExtensionBase) {
        onConfigure(extension)
    }

    fun activate(extension: KradleExtensionBase) {
        if (!activated.compareAndSet(false, true)) {
            return
        }
        val otherPresetActive = project.presetRegistry.map.values
            .filter { p -> p !== this }
            .any { p -> p.isActive }
        if (otherPresetActive) {
            throw GradleException("You can only use one preset")
        }
        onActivate(extension)
    }

    private val isActive
        get() = activated.get()

    protected open fun onConfigure(extension: KradleExtensionBase) = Unit
    protected open fun onActivate(extension: KradleExtensionBase) = Unit
}
