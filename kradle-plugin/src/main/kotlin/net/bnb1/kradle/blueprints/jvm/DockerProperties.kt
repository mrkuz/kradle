package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class DockerProperties : Properties {

    lateinit var baseImage: String
    val ports = mutableSetOf<Int>()
    var withJvmKill: String? = null
    var withStartupScript = false
    var jvmOpts: String? = null
}
