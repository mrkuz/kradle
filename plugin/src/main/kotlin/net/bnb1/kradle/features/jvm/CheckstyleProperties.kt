package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class CheckstyleProperties(project: Project) : Properties(project) {

    val version = property(Catalog.Versions.checkstyle)
    val configFile = property("checkstyle.xml")
}
