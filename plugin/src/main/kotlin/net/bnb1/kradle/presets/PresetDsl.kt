package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase

class PresetDsl(private val preset: Preset) {

    operator fun invoke(action: KradleExtensionBase.() -> Unit = {}) = preset.activate(action)
}
