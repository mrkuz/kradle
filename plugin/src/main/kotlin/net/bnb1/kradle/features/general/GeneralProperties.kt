package net.bnb1.kradle.features.general

import net.bnb1.kradle.features.ConfigurableFeatureImpl
import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class GeneralProperties(project: Project) : Properties(project) {

    val bootstrap = ConfigurableFeatureImpl(BootstrapFeature(), EmptyProperties(project))
        .setParent(GeneralFeatureSet::class)
        .addBlueprint(BootstrapBlueprint(project))
        .register(project)
        .asInterface()
    val git = ConfigurableFeatureImpl(GitFeature(), EmptyProperties(project))
        .setParent(GeneralFeatureSet::class)
        .addBlueprint(GitBlueprint(project))
        .register(project)
        .asInterface()
    val projectProperties = ConfigurableFeatureImpl(ProjectPropertiesFeature(), EmptyProperties(project))
        .setParent(GeneralFeatureSet::class)
        .addBlueprint(ProjectPropertiesBlueprint(project))
        .register(project)
        .asInterface()
    val buildProperties = ConfigurableFeatureImpl(BuildPropertiesFeature(), EmptyProperties(project))
        .setParent(GeneralFeatureSet::class)
        .addBlueprint(BuildPropertiesBlueprint(project))
        .register(project)
        .asInterface()
}
