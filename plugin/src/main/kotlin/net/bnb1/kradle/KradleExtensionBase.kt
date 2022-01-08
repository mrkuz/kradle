package net.bnb1.kradle

import net.bnb1.kradle.dsl.FeatureSetDsl
import net.bnb1.kradle.features.general.GeneralFeatureSet
import net.bnb1.kradle.features.general.GeneralProperties
import net.bnb1.kradle.features.jvm.JvmFeatureSet
import net.bnb1.kradle.features.jvm.JvmProperties
import org.gradle.api.Project

open class KradleExtensionBase(context: KradleContext, project: Project) {

    private val _general by context { GeneralFeatureSet(project) }
    private val _generalProperties by context { GeneralProperties(context, project) }
    val general = FeatureSetDsl(context.get(), _general, _generalProperties)

    private val _jvm by context { JvmFeatureSet(project) }
    private val _jvmProperties by context { JvmProperties(context, project) }
    val jvm = FeatureSetDsl(context.get(), _jvm, _jvmProperties)
}
