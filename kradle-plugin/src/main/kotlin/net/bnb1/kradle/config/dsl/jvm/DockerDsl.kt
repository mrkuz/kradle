package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.dsl.ValueSet

class DockerDsl(properties: AllProperties) {

    val baseImage = Value(properties.docker.baseImage) { properties.docker.baseImage = it }
    val imageName = Optional<String>(null) { properties.docker.imageName = it }
    val allowInsecureRegistries = Flag { properties.docker.allowInsecureRegistries = it }
    val ports = ValueSet(properties.docker.ports)
    val withJvmKill = Optional(Catalog.Versions.jvmKill) { properties.docker.withJvmKill = it }

    val withStartupScript = Flag { properties.docker.withStartupScript = it }
    val withAppSh = withStartupScript
    val startupScript = withStartupScript

    val jvmOpts = Optional<String>(null) { properties.docker.jvmOpts = it }
    val javaOpts = jvmOpts
}
