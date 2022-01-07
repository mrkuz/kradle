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

    val bootstrap = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { BootstrapFeature() }
        .properties { EmptyProperties() }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(_bootstrapBlueprint)
        .build()
    val git = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { GitFeature() }
        .properties { EmptyProperties() }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(_gitBlueprint)
        .build()
    val projectProperties = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { ProjectPropertiesFeature() }
        .properties { EmptyProperties() }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(_projectPropertiesBlueprint)
        .build()
    val buildProperties = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { BuildPropertiesFeature() }
        .properties { EmptyProperties() }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(_buildPropertiesBlueprint)
        .build()
}
