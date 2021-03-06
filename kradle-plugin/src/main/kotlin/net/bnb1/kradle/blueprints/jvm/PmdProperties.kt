package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class PmdProperties(var version: String) : Properties {

    val ruleSets = RuleSets()

    class RuleSets : Properties {

        var bestPractices = false
        var codeStyle = false
        var design = false
        var documentation = false
        var errorProne = false
        var multithreading = false
        var performance = false
        var security = false
    }
}
