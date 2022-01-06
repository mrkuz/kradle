package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class BenchmarkProperties(project: Project) : Properties(project) {

    val jmhVersion = value(Catalog.Versions.jmh)
}
