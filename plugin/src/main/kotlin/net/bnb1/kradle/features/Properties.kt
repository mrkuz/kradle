package net.bnb1.kradle.features

import net.bnb1.kradle.dsl.FlagDsl
import net.bnb1.kradle.dsl.FlagsDsl
import net.bnb1.kradle.dsl.PropertyDsl
import net.bnb1.kradle.dsl.SetPropertyDsl
import net.bnb1.kradle.dsl.VersionDsl
import org.gradle.api.Project

/**
 * Properties are used to configure [feature sets][FeatureSet] and [features][Feature].
 */
open class Properties(protected val project: Project) {

    val factory = project.objects

    fun flag() = FlagDsl(factory.property(Boolean::class.java))

    fun flags(invert: Boolean = false) = FlagsDsl(factory.setProperty(String::class.java), invert)

    fun version(defaultVersion: String) = VersionDsl(
        factory.property(String::class.java), defaultVersion, defaultVersion
    )

    fun optionalVersion(suggestedVersion: String) = VersionDsl(
        factory.property(String::class.java), null, suggestedVersion
    )

    inline fun <reified T : Any> property(defaultValue: T?) =
        PropertyDsl<T>(factory.property(T::class.java), defaultValue)

    inline fun <reified T : Any> property() = PropertyDsl<T>(factory.property(T::class.java), null)

    inline fun <reified T : Any> setProperty() =
        SetPropertyDsl<T>(factory.setProperty(T::class.java))
}
