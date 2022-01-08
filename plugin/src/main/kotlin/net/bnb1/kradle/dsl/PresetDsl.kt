package net.bnb1.kradle.dsl

import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.presets.Preset

class PresetDsl(private val preset: Preset) {

    operator fun invoke(action: KradleExtensionBase.() -> Unit = {}) = preset.activate(action)
}
