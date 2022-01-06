package net.bnb1.kradle.features

import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Flags
import net.bnb1.kradle.dsl.OptionalValue
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.dsl.ValueSet
import org.gradle.api.Project

/**
 * Properties are used to configure [feature sets][FeatureSet] and [features][Feature].
 */
open class Properties(protected val project: Project) {

    val factory = project.objects

    fun flag() = Flag(factory.property(Boolean::class.java))

    fun flags(invert: Boolean = false) = Flags(factory.setProperty(String::class.java), invert)

    inline fun <reified T : Any> value(defaultValue: T?) =
        Value<T>(factory.property(T::class.java), defaultValue)

    inline fun <reified T : Any> value() = Value<T>(factory.property(T::class.java), null)

    fun optional(suggestion: String) = OptionalValue(factory.property(String::class.java), suggestion)

    inline fun <reified T : Any> valueSet() =
        ValueSet<T>(factory.setProperty(T::class.java))
}
