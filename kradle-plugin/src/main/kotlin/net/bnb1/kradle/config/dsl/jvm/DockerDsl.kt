package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.blueprints.jvm.JibProperties
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.dsl.ValueSet

class DockerDsl(properties: JibProperties) {

    val baseImage = Value(properties.baseImage) { properties.baseImage = it }
    val imageName = Optional<String>(null) { properties.imageName = it }
    val allowInsecureRegistries = Flag { properties.allowInsecureRegistries = it }
    val ports = ValueSet(properties.ports)
    val withJvmKill = Optional(Catalog.Versions.jvmKill) { properties.withJvmKill = it }

    val withStartupScript = Flag { properties.withStartupScript = it }
    val withAppSh = withStartupScript
    val startupScript = withStartupScript

    val jvmOpts = Optional<String>(null) { properties.jvmOpts = it }
    val javaOpts = jvmOpts
}
