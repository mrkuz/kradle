package net.bnb1.kradle.core.dsl

import net.bnb1.kradle.core.Preset

class PresetDsl<T : ExtensionDsl>(private val preset: Preset<T>) {

    operator fun invoke(action: T.() -> Unit = {}) = preset.activate(action)
}
