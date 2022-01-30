package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.dsl.Properties

class LintProperties : Properties() {

    val ignoreFailures = flag()
}
