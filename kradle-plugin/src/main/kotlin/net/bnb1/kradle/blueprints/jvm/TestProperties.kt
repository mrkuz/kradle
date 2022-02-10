package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class TestProperties : Properties {

    var prettyPrint = false
    var showStandardStreams = false

    var withIntegrationTests = false
    var withFunctionalTests = false
    var withCustomTests = mutableSetOf<String>()

    var useArchUnit: String? = null
    var useTestcontainers: String? = null
}
