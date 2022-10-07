package net.bnb1.kradle.blueprints.jvm

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer
import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

class SpringBootShadowBlueprint(project: Project) : Blueprint(project) {

    lateinit var extendsTask: String

    override fun doConfigure() {
        project.tasks.named<ShadowJar>(extendsTask).configure {
            // See: https://cloud.spring.io/spring-cloud-function/reference/html/spring-cloud-function.html#_gradle
            mergeServiceFiles()
            append("META-INF/spring.handlers")
            append("META-INF/spring.schemas")
            append("META-INF/spring.tooling")
            transform(
                PropertiesFileTransformer().apply {
                    paths = listOf("META-INF/spring.factories")
                    mergeStrategy = "append"
                }
            )
        }
    }
}
