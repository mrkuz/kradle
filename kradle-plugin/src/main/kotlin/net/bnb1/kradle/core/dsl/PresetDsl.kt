package net.bnb1.kradle.core.dsl

import net.bnb1.kradle.core.Preset

class PresetDsl<T : Any>(private val target: T, private val preset: Preset<T>) {

    operator fun invoke(action: T.() -> Unit = {}) = activate(action)

    fun activate(action: T.() -> Unit = {}) {
        preset.configure(target)
        action(target)
        preset.activate(target)
    }
}
