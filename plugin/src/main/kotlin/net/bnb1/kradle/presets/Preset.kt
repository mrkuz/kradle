package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.presetRegistry
import org.gradle.api.GradleException
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean

open class Preset(private val project: Project) {

    private val activated = AtomicBoolean(false)

    fun configure(extension: KradleExtensionBase) {
        onConfigure(extension)
    }

    fun activate(extension: KradleExtensionBase) {
        val otherPresetActive = project.presetRegistry.map.values.count { p -> p.isActive() } > 0
        if (otherPresetActive) {
            throw GradleException("You can only use one preset")
        }

        if (!activated.compareAndSet(false, true)) {
            return
        }
        onActivate(extension)
    }

    fun isActive() = activated.get()

    protected open fun onConfigure(extension: KradleExtensionBase) = Unit
    protected open fun onActivate(extension: KradleExtensionBase) = Unit
}
