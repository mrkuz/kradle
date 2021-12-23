package net.bnb1.kradle.features.jvm

import Catalog
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project

class BenchmarkProperties(project: Project) : Properties(project) {

    val jmhVersion = property(factory.property(Catalog.Versions.jmh))
}
