package net.bnb1.kradle.features

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class Properties(protected val project: Project) {

    protected val factory = project.objects

    fun <T : Any> property(property: Property<T>) = ConfigurableProperty(property)
}
