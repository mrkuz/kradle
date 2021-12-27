package net.bnb1.kradle.features.general

import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.FeatureDslImpl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class GeneralProperties(project: Project) : Properties(project) {

    val bootstrap = FeatureDslImpl(BootstrapFeature(), EmptyProperties(project))
        .setParent(GeneralFeatureSet::class)
        .addBlueprint(BootstrapBlueprint(project))
        .register(project)
        .asInterface()
    val git = FeatureDslImpl(GitFeature(), EmptyProperties(project))
        .setParent(GeneralFeatureSet::class)
        .addBlueprint(GitBlueprint(project))
        .register(project)
        .asInterface()
    val projectProperties = FeatureDslImpl(ProjectPropertiesFeature(), EmptyProperties(project))
        .setParent(GeneralFeatureSet::class)
        .addBlueprint(ProjectPropertiesBlueprint(project))
        .register(project)
        .asInterface()
    val buildProperties = FeatureDslImpl(BuildPropertiesFeature(), EmptyProperties(project))
        .setParent(GeneralFeatureSet::class)
        .addBlueprint(BuildPropertiesBlueprint(project))
        .register(project)
        .asInterface()
}
