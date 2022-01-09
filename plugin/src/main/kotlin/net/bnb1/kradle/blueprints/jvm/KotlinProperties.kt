package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.dsl.Properties

class KotlinProperties : Properties() {

    val kotlinxCoroutinesVersion = optional<String>()
}
