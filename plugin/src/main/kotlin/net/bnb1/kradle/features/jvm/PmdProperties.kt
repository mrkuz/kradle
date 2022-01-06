package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.Configurable
import net.bnb1.kradle.dsl.FlagDsl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class PmdProperties(project: Project) : Properties(project) {

    val version = version(Catalog.Versions.pmd)
    val ruleSets = RuleSets(project.objects)

    class RuleSets(factory: ObjectFactory) : Configurable<RuleSets> {

        val bestPractices = FlagDsl(factory.property(Boolean::class.java))
        val codeStyle = FlagDsl(factory.property(Boolean::class.java))
        val design = FlagDsl(factory.property(Boolean::class.java))
        val documentation = FlagDsl(factory.property(Boolean::class.java))
        val errorProne = FlagDsl(factory.property(Boolean::class.java)).apply { enable() }
        val multithreading = FlagDsl(factory.property(Boolean::class.java)).apply { enable() }
        val performance = FlagDsl(factory.property(Boolean::class.java)).apply { enable() }
        val security = FlagDsl(factory.property(Boolean::class.java)).apply { enable() }
    }
}
