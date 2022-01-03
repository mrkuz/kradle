package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project

class DetektProperties(project: Project) : Properties(project) {

    val version = property(factory.property(Catalog.Versions.detekt))
    val configFile = property(factory.property("detekt-config.yml"))
}
