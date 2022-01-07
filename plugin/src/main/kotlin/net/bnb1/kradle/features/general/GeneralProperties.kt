package net.bnb1.kradle.features.general

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.FeatureDsl
import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class GeneralProperties(context: KradleContext, project: Project) : Properties() {

    private val _bootstrapBlueprint by context { BootstrapBlueprint(project) }
    private val _gitBlueprint by context { GitBlueprint(project) }
    private val _projectPropertiesBlueprint by context { ProjectPropertiesBlueprint(project) }
    private val _buildPropertiesBlueprint by context { BuildPropertiesBlueprint(project) }

    private val _bootstrap by context { BootstrapFeature() }
    private val _git by context { GitFeature() }
    private val _projectProperties by context { ProjectPropertiesFeature() }
    private val _buildProperties by context { BuildPropertiesFeature() }

    init {
        _buildPropertiesBlueprint.dependsOn += _buildProperties

        _bootstrap += _bootstrapBlueprint
        _git += _gitBlueprint
        _projectProperties += _projectPropertiesBlueprint

        context.get<GeneralFeatureSet>() += setOf(
            _bootstrap,
            _git,
            _projectProperties,
            _buildProperties
        )
    }

    val bootstrap = FeatureDsl(_bootstrap, EmptyProperties())
    val git = FeatureDsl(_git, EmptyProperties())
    val projectProperties = FeatureDsl(_projectProperties, EmptyProperties())
    val buildProperties = FeatureDsl(_buildProperties, EmptyProperties())
}
