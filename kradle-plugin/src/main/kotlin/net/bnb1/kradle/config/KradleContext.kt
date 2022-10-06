package net.bnb1.kradle.config

import net.bnb1.kradle.blueprints.jvm.DevelopmentModeBlueprint
import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.core.FeatureSet
import net.bnb1.kradle.core.Properties
import net.bnb1.kradle.support.Registry
import org.gradle.api.Project

@Suppress("LongMethod", "ComplexMethod")
class KradleContext(project: Project) {

    private val registry = Registry()

    val properties = AllProperties(registry)
    val features = AllFeatures(registry)
    val blueprints = AllBlueprints(registry, properties, project)
    val featureSets = AllFeatureSets(registry)
    val presets = AllPresets(registry)

    fun featuresAsList() = registry.withType<Feature>()
    fun featuresSetsAsList() = registry.withType<FeatureSet>()
    fun propertiesAsList() = registry.withType<Properties>()

    fun initialize() {
        initializeSubFeatures()

        features.bootstrap.also { me ->
            me belongsTo featureSets.general
            me += blueprints.bootstrap.also {
                it.taskName = features.bootstrap.defaultTaskName
            }
        }
        features.git.also { me ->
            me belongsTo featureSets.general
            me activatesAfter features.bootstrap
            me += blueprints.git.also {
                it.extendsBootstrapTask = features.bootstrap.defaultTaskName
            }
        }
        features.buildProfiles.also { me ->
            me belongsTo featureSets.general
            me += blueprints.buildProfiles
        }
        features.projectProperties.also { me ->
            me belongsTo featureSets.general
            me activatesAfter features.buildProfiles
            me += blueprints.projectProperties.also {
                it.withBuildProfiles = { features.buildProfiles.isEnabled }
            }
        }
        features.buildProperties.also { me ->
            me belongsTo featureSets.general
        }
        features.scripts.also { me ->
            me belongsTo featureSets.general
            me += blueprints.scripts
        }
        features.helm.also { me ->
            me belongsTo featureSets.general
            me += blueprints.helm
        }

        features.kotlin.also { me ->
            me belongsTo featureSets.jvm
            me activatesAfter features.bootstrap
            me += setOf(
                blueprints.java,
                blueprints.kotlin,
                blueprints.allOpen,
                blueprints.bootstrap.also {
                    it dependsOn features.bootstrap
                },
                blueprints.kotlinAppBootstrap.also {
                    it dependsOn features.bootstrap
                    it dependsOn features.application
                    it disabledBy features.springBoot
                    it.extendsTask = features.bootstrap.defaultTaskName
                },
                blueprints.kotlinLibBootstrap.also {
                    it dependsOn features.bootstrap
                    it dependsOn features.library
                    it.extendsTask = features.bootstrap.defaultTaskName
                },
                blueprints.buildProperties.also {
                    it dependsOn features.buildProperties
                    it.withGit = { features.git.isEnabled }
                    it.withBuildProfiles = { features.buildProfiles.isEnabled }
                }
            )
        }
        features.java.also { me ->
            me belongsTo featureSets.jvm
            me activatesAfter features.bootstrap
            me += setOf(
                blueprints.java.also {
                    it.extendsBootstrapTask = features.bootstrap.defaultTaskName
                },
                blueprints.bootstrap.also {
                    it dependsOn features.bootstrap
                },
                blueprints.javaAppBootstrap.also {
                    it dependsOn features.bootstrap
                    it dependsOn features.application
                    it disabledBy features.springBoot
                    it.extendsTask = features.bootstrap.defaultTaskName
                },
                blueprints.javaLibBootstrap.also {
                    it dependsOn features.bootstrap
                    it dependsOn features.library
                    it.extendsTask = features.bootstrap.defaultTaskName
                },
                blueprints.buildProperties
            )
        }
        features.application.also { me ->
            me belongsTo featureSets.jvm
            me conflictsWith features.library
            me += blueprints.application.also {
                it.withBuildProfiles = { features.buildProfiles.isEnabled }
            }
        }
        features.library.also { me ->
            me belongsTo featureSets.jvm
            me conflictsWith features.application
            me += setOf(
                blueprints.library,
                blueprints.mavenPublish
            )
        }
        features.dependencyUpdates.also { me ->
            me belongsTo featureSets.jvm
            me += blueprints.dependencyUpdates
        }
        features.dependencies.also { me ->
            me belongsTo featureSets.jvm
            me += setOf(
                blueprints.dependencyUpdates,
                blueprints.dependencies
            )
        }
        features.vulnerabilityScan.also { me ->
            me belongsTo featureSets.jvm
            me += blueprints.owaspDependencyCheck
        }
        features.lint.also { me ->
            me belongsTo featureSets.jvm
            me activatesAfter features.test
            me activatesAfter features.benchmark
            me += setOf(
                blueprints.lint.also {
                    it.taskName = features.lint.defaultTaskName
                }
            )
        }
        features.codeAnalysis.also { me ->
            me belongsTo featureSets.jvm
            me activatesAfter features.test
            me activatesAfter features.benchmark
            me += setOf(
                blueprints.codeAnalysis.also {
                    it.taskName = features.codeAnalysis.defaultTaskName
                }
            )
        }
        features.developmentMode.also { me ->
            me belongsTo featureSets.jvm
            me requires features.application
            me += blueprints.developmentMode.also {
                it.withBuildProfiles = { features.buildProfiles.isEnabled }
            }
        }
        features.test.also { me ->
            me belongsTo featureSets.jvm
            me += setOf(
                blueprints.test.also {
                    it.withJunitJupiter = { features.junitJupiter.isEnabled }
                    it.withBuildProfiles = { features.buildProfiles.isEnabled }
                },
                blueprints.kotlinTest.also {
                    it dependsOn features.kotlin
                    it.withJunitJupiter = { features.junitJupiter.isEnabled }
                }
            )
        }
        features.codeCoverage.also { me ->
            me belongsTo featureSets.jvm
            me activatesAfter features.test
            me += blueprints.codeCoverage.also {
                it.taskName = features.codeCoverage.defaultTaskName
            }
        }
        features.benchmark.also { me ->
            me belongsTo featureSets.jvm
            me += setOf(
                blueprints.allOpen.also { it dependsOn features.kotlin },
                blueprints.benchmarks.also {
                    it.withBuildProfiles = { features.buildProfiles.isEnabled }
                }
            )
        }
        features.packaging.also { me ->
            me belongsTo featureSets.jvm
            me activatesAfter features.application
            me += setOf(
                blueprints.packaging,
                blueprints.packageApplication.also { it dependsOn features.application },
                blueprints.shadow.also { it dependsOn features.application }
            )
        }
        features.docker.also { me ->
            me belongsTo featureSets.jvm
            me requires features.application
            me += blueprints.jib
        }
        features.documentation.also { me ->
            me belongsTo featureSets.jvm
            me += blueprints.dokka
        }

        features.springBoot.also { me ->
            me belongsTo featureSets.jvm
            me += setOf(
                blueprints.springBoot.also {
                    it.withDevelopmentMode = { features.developmentMode.isEnabled }
                    it.developmentConfiguration = DevelopmentModeBlueprint.CONFIGURATION_NAME
                },
                blueprints.bootstrap.also {
                    it dependsOn features.bootstrap
                },
                blueprints.springBootJavaAppBootstrap.also {
                    it dependsOn features.java
                    it dependsOn features.bootstrap
                    it dependsOn features.application
                    it.extendsTask = features.bootstrap.defaultTaskName
                },
                blueprints.springBootKotlinAppBootstrap.also {
                    it dependsOn features.kotlin
                    it dependsOn features.bootstrap
                    it dependsOn features.application
                    it.extendsTask = features.bootstrap.defaultTaskName
                }
            )
        }
    }

    private fun initializeSubFeatures() {
        features.checkstyle.also { me ->
            me.enable()
            me belongsTo featureSets.jvm
            me activatesAfter features.bootstrap
            me activatesAfter features.lint
            me += blueprints.checkstyle.also {
                it dependsOn features.lint
                it dependsOn features.java
                it.extendsTask = features.lint.defaultTaskName
                it.extendsBootstrapTask = features.bootstrap.defaultTaskName
            }
        }
        features.pmd.also { me ->
            me.enable()
            me belongsTo featureSets.jvm
            me activatesAfter features.bootstrap
            me activatesAfter features.codeAnalysis
            me += blueprints.pmd.also {
                it dependsOn features.codeAnalysis
                it dependsOn features.java
                it.extendsTask = features.codeAnalysis.defaultTaskName
            }
        }
        features.spotBugs.also { me ->
            me.enable()
            me belongsTo featureSets.jvm
            me activatesAfter features.codeAnalysis
            me += blueprints.spotBugs.also {
                it dependsOn features.codeAnalysis
                it dependsOn features.java
                it.extendsTask = features.codeAnalysis.defaultTaskName
            }
        }
        features.ktlint.also { me ->
            me.enable()
            me belongsTo featureSets.jvm
            me activatesAfter features.lint
            me += blueprints.ktlint.also {
                it dependsOn features.lint
                it dependsOn features.kotlin
                it.extendsTask = features.lint.defaultTaskName
            }
        }
        features.detekt.also { me ->
            me.enable()
            me belongsTo featureSets.jvm
            me activatesAfter features.codeAnalysis
            me += blueprints.detekt.also {
                it dependsOn features.codeAnalysis
                it dependsOn features.kotlin
                it.extendsTask = features.codeAnalysis.defaultTaskName
                it.extendsBootstrapTask = features.bootstrap.defaultTaskName
            }
        }
        features.jacoco.also { me ->
            me.disable()
            me belongsTo featureSets.jvm
            me activatesAfter features.test
            me += setOf(
                // Compatibility
                blueprints.codeCoverage,
                blueprints.jacoco.also {
                    // Compatibility: no dependsOn
                    it.extendsTask = features.codeCoverage.defaultTaskName
                }
            )
        }
        features.kover.also { me ->
            me.enable()
            me belongsTo featureSets.jvm
            me activatesAfter features.test
            me activatesAfter features.codeCoverage
            me += blueprints.kover.also {
                it dependsOn features.codeCoverage
                it.extendsTask = features.codeCoverage.defaultTaskName
            }
        }
        features.junitJupiter.also { me ->
            me.enable()
            me belongsTo featureSets.jvm
            me activatesAfter features.test
            me += blueprints.junitJupiter.also {
                it dependsOn features.test
            }
        }
    }
}
