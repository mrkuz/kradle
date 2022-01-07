package net.bnb1.kradle.dsl

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.presets.Preset
import org.gradle.api.Project

class PresetDsl(private val preset: Preset, private val project: Project) {

    operator fun invoke(action: KradleExtensionBase.() -> Unit = {}) = activate(action)

    fun activate(action: KradleExtensionBase.() -> Unit = {}) {
        val extension = project.extensions.getByType(KradleExtension::class.java)
        preset.configure(extension)
        action(extension)
        preset.activate(extension)
    }
}
