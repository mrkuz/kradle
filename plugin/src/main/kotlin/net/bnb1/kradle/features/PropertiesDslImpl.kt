package net.bnb1.kradle.features

import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project

class PropertiesDslImpl<P : Properties>(private val properties: P) :
    PropertiesDsl<P> {

    fun register(project: Project): PropertiesDslImpl<P> {
        project.propertiesRegistry.register(properties)
        return this
    }

    fun asInterface() = this as PropertiesDsl<P>

    override fun configure(action: P.() -> Unit) = action(properties)
}
