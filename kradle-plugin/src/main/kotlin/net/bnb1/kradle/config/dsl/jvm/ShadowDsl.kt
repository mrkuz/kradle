package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.ShadowProperties
import net.bnb1.kradle.dsl.Flag

class ShadowDsl(properties: ShadowProperties) {

    val minimize = Flag { properties.minimize = it }
}
