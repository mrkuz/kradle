package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.dsl.ValueSet

class DockerDsl(properties: AllProperties) {

    val baseImage = Value(properties.jib.baseImage) { properties.jib.baseImage = it }
    val imageName = Optional<String>(null) { properties.jib.imageName = it }
    val allowInsecureRegistries = Flag { properties.jib.allowInsecureRegistries = it }
    val ports = ValueSet(properties.jib.ports)
    val withJvmKill = Optional(Catalog.Versions.jvmKill) { properties.jib.withJvmKill = it }

    val withStartupScript = Flag { properties.jib.withStartupScript = it }
    val withAppSh = withStartupScript
    val startupScript = withStartupScript

    val jvmOpts = Optional<String>(null) { properties.jib.jvmOpts = it }
    val javaOpts = jvmOpts
}
