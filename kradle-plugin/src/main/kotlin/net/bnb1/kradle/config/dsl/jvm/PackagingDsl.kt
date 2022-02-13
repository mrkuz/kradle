package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class PackagingDsl(properties: AllProperties) {

    val uberJar = Configurable(ShadowDsl(properties.shadow))
}
