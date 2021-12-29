package net.bnb1.kradle.features

import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project

class PropertiesDsl<P : Properties> private constructor(private val properties: P) {

    operator fun invoke(action: P.() -> Unit = {}) = configure(action)

    fun configure(action: P.() -> Unit = {}) = action(properties)

    class Builder<P : Properties>(private val project: Project) {

        private var supplier: ((Project) -> P)? = null

        fun properties(supplier: (Project) -> P): Builder<P> {
            this.supplier = supplier
            return this
        }

        fun build(): PropertiesDsl<P> {
            val properties = supplier!!(project).also { project.propertiesRegistry.register(it) }
            return PropertiesDsl(properties)
        }
    }
}
