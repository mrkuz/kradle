package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.presetRegistry
import org.gradle.api.Project

class PresetDsl private constructor(private val preset: Preset, private val project: Project) {

    operator fun invoke(action: KradleExtensionBase.() -> Unit = {}) = activate(action)

    fun activate(action: KradleExtensionBase.() -> Unit = {}) {
        val extension = project.extensions.getByType(KradleExtension::class.java)
        preset.configure(extension)
        action(extension)
        preset.activate(extension)
    }

    class Builder(private val project: Project) {

        private var supplier: ((Project) -> Preset)? = null

        fun preset(supplier: (Project) -> Preset): Builder {
            this.supplier = supplier
            return this
        }

        fun build(): PresetDsl {
            val preset = supplier!!(project).also { project.presetRegistry.register(it) }
            return PresetDsl(preset, project)
        }
    }
}
