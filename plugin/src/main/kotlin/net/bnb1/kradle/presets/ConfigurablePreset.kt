package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase

interface ConfigurablePreset {

    operator fun invoke(action: KradleExtensionBase.() -> Unit = {}) = activate(action)

    fun activate(action: KradleExtensionBase.() -> Unit = {})
}
