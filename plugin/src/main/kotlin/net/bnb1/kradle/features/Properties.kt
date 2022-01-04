package net.bnb1.kradle.features

import org.gradle.api.Project

/**
 * Properties are used to configure [feature sets][FeatureSet] and [features][Feature].
 */
open class Properties(protected val project: Project) {

    val factory = project.objects

    inline fun <reified T : Any> property(defaultValue: T?) =
        PropertyDsl<T>(factory.property(T::class.java), defaultValue)

    inline fun <reified T : Any> property() = PropertyDsl<T>(factory.property(T::class.java), null)
}
