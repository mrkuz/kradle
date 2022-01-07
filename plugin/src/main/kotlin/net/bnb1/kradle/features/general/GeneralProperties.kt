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

    val bootstrap = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { _bootstrap }
        .properties { EmptyProperties() }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(_bootstrapBlueprint)
        .build()
    val git = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { _git }
        .properties { EmptyProperties() }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(_gitBlueprint)
        .build()
    val projectProperties = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { _projectProperties }
        .properties { EmptyProperties() }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(_projectPropertiesBlueprint)
        .build()
    val buildProperties = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { _buildProperties }
        .properties { EmptyProperties() }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(_buildPropertiesBlueprint)
        .build()
}
