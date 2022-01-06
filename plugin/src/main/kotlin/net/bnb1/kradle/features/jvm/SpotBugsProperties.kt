package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class SpotBugsProperties(project: Project) : Properties(project) {

    val version = value(Catalog.Versions.spotbugs)

    val useFindSecBugs = optional(Catalog.Versions.findSecBugs)
    val useFbContrib = optional(Catalog.Versions.fbContrib)
}
