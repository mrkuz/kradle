package net.bnb1.kradle.features

import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project

class ConfigurablePropertiesImpl<P : Properties>(private val properties: P) :
    ConfigurableProperties<P> {

    fun register(project: Project): ConfigurablePropertiesImpl<P> {
        project.propertiesRegistry.register(properties)
        return this
    }

    fun asInterface() = this as ConfigurableProperties<P>

    override fun configure(action: P.() -> Unit) = action(properties)
}
