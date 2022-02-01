package net.bnb1.kradle.core.dsl

import net.bnb1.kradle.core.Blueprint

class BlueprintDsl<T : Any>(private val blueprint: Blueprint, private val target: T) {

    operator fun invoke(enable: Boolean = true) {
        if (enable) {
            enable()
        } else {
            disable()
        }
    }

    operator fun invoke(action: T.() -> Unit) = enable(action)

    fun enable(action: T.() -> Unit = {}) {
        action(target)
        blueprint.enable()
    }

    fun disable() {
        blueprint.disable()
    }
}
