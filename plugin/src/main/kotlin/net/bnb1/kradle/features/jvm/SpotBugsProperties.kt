package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.empty
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project

class SpotBugsProperties(project: Project) : Properties(project) {

    val version = property(factory.property(Catalog.Versions.spotbugs))

    val findSecBugs = property(factory.empty<String>())
    fun useFindSecBugs(version: String = Catalog.Versions.findSecBugs) = findSecBugs.set(version)

    val fbContrib = property(factory.empty<String>())
    fun useFbContrib(version: String = Catalog.Versions.fbContrib) = fbContrib.set(version)
}
