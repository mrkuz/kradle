package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.features.Properties

class PackagingProperties(context: KradleContext) : Properties() {

    val uberJar = Configurable(context.get<ShadowProperties>())
}
