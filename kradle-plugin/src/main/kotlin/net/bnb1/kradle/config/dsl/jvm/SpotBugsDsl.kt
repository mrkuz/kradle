package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.blueprints.jvm.SpotBugsProperties
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.dsl.Value

class SpotBugsDsl(properties: SpotBugsProperties) {

    val version = Value(properties.version) { properties.version = it }

    val useFindSecBugs = Optional(Catalog.Versions.findSecBugs) { properties.useFindSecBugs = it }
    val useFbContrib = Optional(Catalog.Versions.fbContrib) { properties.useFbContrib = it }
}
