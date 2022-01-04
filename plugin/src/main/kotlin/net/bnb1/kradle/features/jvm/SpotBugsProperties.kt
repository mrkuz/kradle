package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class SpotBugsProperties(project: Project) : Properties(project) {

    val version = property(Catalog.Versions.spotbugs)

    val findSecBugs = property<String>()
    fun useFindSecBugs(version: String = Catalog.Versions.findSecBugs) = findSecBugs.set(version)

    val fbContrib = property<String>()
    fun useFbContrib(version: String = Catalog.Versions.fbContrib) = fbContrib.set(version)
}
