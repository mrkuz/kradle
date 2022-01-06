package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class KtlintProperties(project: Project) : Properties(project) {

    val version = value(Catalog.Versions.ktlint)
    val rules = flags(true)
}
