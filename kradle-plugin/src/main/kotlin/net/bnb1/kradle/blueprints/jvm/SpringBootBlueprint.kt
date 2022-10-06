package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.implementation
import net.bnb1.kradle.testImplementation
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.allopen.gradle.SpringGradleSubplugin

class SpringBootBlueprint(project: Project) : Blueprint(project) {

    lateinit var springBootProperties: SpringBootProperties

    lateinit var withKotlin: () -> Boolean
    lateinit var withBuildProfiles: () -> Boolean
    lateinit var withDevelopmentMode: () -> Boolean
    lateinit var developmentConfiguration: String

    override fun doApplyPlugins() {
        project.apply(SpringGradleSubplugin::class.java)
    }

    override fun doAddDependencies() {
        project.dependencies {
            implementation(platform("${Catalog.Dependencies.Platform.springBoot}:${springBootProperties.version}"))
            implementation("${Catalog.Dependencies.springBootStarter}:${springBootProperties.version}")
            testImplementation("${Catalog.Dependencies.Test.springBootStarterTest}:${springBootProperties.version}")
            springBootProperties.useWeb?.let {
                implementation(
                    "${Catalog.Dependencies.springBootStarterWeb}:$it"
                )
            }
            springBootProperties.useWebFlux?.let {
                implementation(
                    "${Catalog.Dependencies.springBootStarterWebFlux}:$it"
                )
            }
            springBootProperties.useActuator?.let {
                implementation(
                    "${Catalog.Dependencies.springBootStarterActuator}:$it"
                )
            }

            if (withDevelopmentMode()) {
                springBootProperties.withDevTools?.let {
                    add(
                        developmentConfiguration,
                        "${Catalog.Dependencies.Tools.springBootDevtools}:$it"
                    )
                }
            }

            if (withKotlin()) {
                if (springBootProperties.useWeb != null) {
                    implementation(Catalog.Dependencies.jacksonModuleKotlin)
                }
                if (springBootProperties.useWebFlux != null) {
                    implementation(Catalog.Dependencies.jacksonModuleKotlin)
                    implementation(Catalog.Dependencies.reactorKotlinExtensions)
                    implementation(Catalog.Dependencies.kotlinxCoroutinesReactor)
                    testImplementation(Catalog.Dependencies.Test.reactorTest)
                }
            }
        }
    }

    override fun doConfigure() {
        project.tasks.withType<JavaExec> {
            if (withBuildProfiles()) {
                environment("SPRING_PROFILES_ACTIVE", project.extra["profile"].toString())
            }
        }
    }
}
