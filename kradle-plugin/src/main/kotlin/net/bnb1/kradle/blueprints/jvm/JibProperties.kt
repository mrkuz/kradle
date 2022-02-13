package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class JibProperties(var baseImage: String) : Properties {

    var imageName: String? = null
    var allowInsecureRegistries = false
    val ports = mutableSetOf<Int>()
    var withJvmKill: String? = null
    var withStartupScript = false
    var jvmOpts: String? = null
}
