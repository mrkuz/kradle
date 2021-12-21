package net.bnb1.kradle

import net.bnb1.kradle.features.ConfigurableFeatureImpl
import net.bnb1.kradle.features.FeatureRegistry
import net.bnb1.kradle.features.PropertiesRegistry
import net.bnb1.kradle.features.general.GeneralBlueprint
import net.bnb1.kradle.features.general.GeneralFeature
import net.bnb1.kradle.features.general.GeneralProperties
import net.bnb1.kradle.features.jvm.JvmBlueprint
import net.bnb1.kradle.features.jvm.JvmFeature
import net.bnb1.kradle.features.jvm.JvmProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import javax.inject.Inject

open class KradleExtension @Inject constructor(private val project: Project) {

    init {
        project.extra["featureRegistry"] = FeatureRegistry()
        project.extra["propertiesRegistry"] = PropertiesRegistry()
    }

    val general = ConfigurableFeatureImpl(GeneralFeature(), GeneralProperties(project))
        .addBlueprint(GeneralBlueprint(project))
        .register(project)
        .asInterface()
    val jvm = ConfigurableFeatureImpl(JvmFeature(), JvmProperties(project))
        .addBlueprint(JvmBlueprint(project))
        .register(project)
        .asInterface()
}
