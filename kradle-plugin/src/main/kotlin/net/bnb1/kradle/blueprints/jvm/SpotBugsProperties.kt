package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class SpotBugsProperties : Properties {

    lateinit var version: String

    var useFindSecBugs: String? = null
    var useFbContrib: String? = null
}
