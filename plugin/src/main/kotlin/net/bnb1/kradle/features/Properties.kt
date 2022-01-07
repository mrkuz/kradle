package net.bnb1.kradle.features

import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Flags
import net.bnb1.kradle.dsl.OptionalValue
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.dsl.ValueSet

/**
 * Properties are used to configure [feature sets][FeatureSet] and [features][Feature].
 */
open class Properties {

    fun flag() = Flag()

    fun flags(invert: Boolean = false) = Flags(invert)

    inline fun <reified T : Any> value(defaultValue: T?) = Value(defaultValue)

    inline fun <reified T : Any> value() = Value<T>(null)

    fun optional(suggestion: String) = OptionalValue(suggestion)

    inline fun <reified T : Any> valueSet() = ValueSet<T>()
}
