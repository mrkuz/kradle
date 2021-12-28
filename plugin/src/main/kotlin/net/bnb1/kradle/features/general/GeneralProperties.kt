package net.bnb1.kradle.features.general

import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.FeatureDsl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class GeneralProperties(project: Project) : Properties(project) {

    val bootstrap = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { BootstrapFeature() }
        .properties { EmptyProperties(it) }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(BootstrapBlueprint(project))
        .build()
    val git = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { GitFeature() }
        .properties { EmptyProperties(it) }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(GitBlueprint(project))
        .build()
    val projectProperties = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { ProjectPropertiesFeature() }
        .properties { EmptyProperties(it) }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(ProjectPropertiesBlueprint(project))
        .build()
    val buildProperties = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { BuildPropertiesFeature() }
        .properties { EmptyProperties(project) }
        .parent(GeneralFeatureSet::class)
        .addBlueprint(BuildPropertiesBlueprint(project))
        .build()
}
