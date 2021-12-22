package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.presetRegistry
import org.gradle.api.Project

class ConfigurablePresetImpl(private val preset: Preset, private val project: Project) : ConfigurablePreset {

    fun asInterface() = this as ConfigurablePreset

    fun register(project: Project): ConfigurablePresetImpl {
        project.presetRegistry.register(preset)
        return this
    }

    override fun activate(action: KradleExtensionBase.() -> Unit) {
        val extension = project.extensions.getByType(KradleExtension::class.java)
        preset.configure(extension)
        action(extension)
        preset.activate(extension)
    }
}
