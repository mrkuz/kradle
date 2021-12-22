package net.bnb1.kradle.features

import kotlin.reflect.KClass

interface FeatureListener {

    fun onFeatureActivate(feature: KClass<out Feature>)
}
