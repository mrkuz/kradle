package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.Configurable
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class PmdProperties(project: Project) : Properties(project) {

    val version = property(Catalog.Versions.pmd)
    val ruleSets = RuleSets()

    class RuleSets : Configurable<RuleSets> {

        private val _enabled = mutableSetOf<String>()
        val enabled
            get() = _enabled.toSet()

        init {
            errorProne()
            multithreading()
            performance()
            security()
        }

        private fun set(name: String, enabled: Boolean) {
            val ruleSet = "category/java/$name.xml"
            if (enabled) {
                _enabled.add(ruleSet)
            } else {
                _enabled.remove(ruleSet)
            }
        }

        fun bestPractices(enabled: Boolean = true) = set("bestpractices", enabled)
        fun codeStyle(enabled: Boolean = true) = set("codestyle", enabled)
        fun design(enabled: Boolean = true) = set("design", enabled)
        fun documentation(enabled: Boolean = true) = set("documentation", enabled)
        fun errorProne(enabled: Boolean = true) = set("errorprone", enabled)
        fun multithreading(enabled: Boolean = true) = set("multithreading", enabled)
        fun performance(enabled: Boolean = true) = set("performance", enabled)
        fun security(enabled: Boolean = true) = set("security", enabled)
    }
}
