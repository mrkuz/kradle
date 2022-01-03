package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.Configurable
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project

class KtlintProperties(project: Project) : Properties(project) {

    val version = property(factory.property(Catalog.Versions.ktlint))
    val rules = Rules()

    class Rules : Configurable<Rules> {

        private val _disabled = mutableSetOf<String>()
        val disabled
            get() = _disabled.toSet()

        fun disable(name: String) = _disabled.add(name)
        fun enable(name: String) = _disabled.remove(name)
    }
}
