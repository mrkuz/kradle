package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.Properties

class KotlinProperties : Properties() {

    val kotlinxCoroutinesVersion = optional<String>()
}
