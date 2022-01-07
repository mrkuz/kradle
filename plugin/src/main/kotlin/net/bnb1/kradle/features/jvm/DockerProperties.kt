package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties

class DockerProperties : Properties() {

    val baseImage = value("bellsoft/liberica-openjdk-alpine:${Catalog.Versions.jvm}")
    val ports = valueSet<Int>()
    val withJvmKill = optional(Catalog.Versions.jvmKill)

    val withAppSh = flag()
    val startupScript = withAppSh

    val javaOpts = value<String>()
    val jvmOpts = javaOpts
}
