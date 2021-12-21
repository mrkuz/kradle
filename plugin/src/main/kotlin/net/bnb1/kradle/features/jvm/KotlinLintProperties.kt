package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class KotlinLintProperties(project: Project) : Properties(project) {

    val ktlintVersion = property(factory.property("0.43.0"))
}