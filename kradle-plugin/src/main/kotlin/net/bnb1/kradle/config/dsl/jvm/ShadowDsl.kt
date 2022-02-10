package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Flag

class ShadowDsl(properties: AllProperties) {

    val minimize = Flag { properties.shadow.minimize = it }
}
