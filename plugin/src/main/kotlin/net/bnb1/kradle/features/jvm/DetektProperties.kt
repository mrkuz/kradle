package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class DetektProperties(project: Project) : Properties(project) {

    val version = version(Catalog.Versions.detekt)
    val configFile = property("detekt-config.yml")
}
