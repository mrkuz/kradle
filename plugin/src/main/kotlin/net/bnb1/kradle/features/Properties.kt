package net.bnb1.kradle.features

import org.gradle.api.Project
import org.gradle.api.provider.Property

/**
 * Properties are used to configure [feature sets][FeatureSet] and [features][Feature].
 */
open class Properties(protected val project: Project) {

    protected val factory = project.objects

    fun <T : Any> property(property: Property<T>) = PropertyDsl(property)
}
