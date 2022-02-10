package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.dsl.Value

class SpotBugsDsl(properties: AllProperties) {

    val version = Value(Catalog.Versions.spotbugs) { properties.spotBugs.version = it }

    val useFindSecBugs = Optional(Catalog.Versions.findSecBugs) { properties.spotBugs.useFindSecBugs = it }
    val useFbContrib = Optional(Catalog.Versions.fbContrib) { properties.spotBugs.useFbContrib = it }
}
