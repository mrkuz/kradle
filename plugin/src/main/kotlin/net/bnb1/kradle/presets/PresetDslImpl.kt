package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.presetRegistry
import org.gradle.api.Project

class PresetDslImpl(private val preset: Preset, private val project: Project) : PresetDsl {

    fun asInterface() = this as PresetDsl

    fun register(project: Project): PresetDslImpl {
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
