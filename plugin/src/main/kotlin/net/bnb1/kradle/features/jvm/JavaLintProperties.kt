package net.bnb1.kradle.features.jvm

import Catalog
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project

class JavaLintProperties(project: Project) : Properties(project) {

    val checkstyleConfigFile = property(factory.property("checkstyle.xml"))
    val checkstyleVersion = property(factory.property(Catalog.Versions.checkstyle))
}
