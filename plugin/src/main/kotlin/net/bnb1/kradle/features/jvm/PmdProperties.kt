package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.Configurable
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class PmdProperties(project: Project) : Properties(project) {

    val version = version(Catalog.Versions.pmd)
    val ruleSets = RuleSets(project.objects)

    class RuleSets(factory: ObjectFactory) : Configurable<RuleSets> {

        val bestPractices = Flag(factory.property(Boolean::class.java))
        val codeStyle = Flag(factory.property(Boolean::class.java))
        val design = Flag(factory.property(Boolean::class.java))
        val documentation = Flag(factory.property(Boolean::class.java))
        val errorProne = Flag(factory.property(Boolean::class.java)).apply { enable() }
        val multithreading = Flag(factory.property(Boolean::class.java)).apply { enable() }
        val performance = Flag(factory.property(Boolean::class.java)).apply { enable() }
        val security = Flag(factory.property(Boolean::class.java)).apply { enable() }
    }
}
