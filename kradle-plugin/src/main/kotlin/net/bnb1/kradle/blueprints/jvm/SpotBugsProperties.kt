package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class SpotBugsProperties(var version: String) : Properties {

    var useFindSecBugs: String? = null
    var useFbContrib: String? = null
}
