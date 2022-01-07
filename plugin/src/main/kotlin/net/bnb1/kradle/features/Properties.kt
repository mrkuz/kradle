package net.bnb1.kradle.features

import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Flags
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.dsl.ValueSet

/**
 * Properties are used to configure [feature sets][FeatureSet] and [features][Feature].
 */
open class Properties {

    fun flag() = Flag()

    fun flags(invert: Boolean = false) = Flags(invert)

    fun <T : Any> value(defaultValue: T?) = Value(defaultValue, null)

    inline fun <reified T : Any> optional() = Value<T>(null, null)

    fun <T : Any> optional(suggestion: T) = Value(null, suggestion)

    inline fun <reified T : Any> valueSet() = ValueSet<T>()
}
