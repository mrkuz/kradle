package net.bnb1.kradle.v1

import net.bnb1.kradle.apply
import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.config.dsl.KradleExtensionDsl
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

/**
 * Provides backwards-compatibility for Kradle v1.
 */
class KradleCompat(
    private val tracer: Tracer,
    private val properties: AllProperties,
    private val blueprints: AllBlueprints,
    private val extension: KradleExtensionDsl,
    private val project: Project,
    private val type: ProjectType
) {

    enum class ProjectType {
        APPLICATION, LIBRARY
    }

    fun activate() {
        configureEager()
        project.afterEvaluate {
            configure()
            extension.run {
                general.activate()
                jvm.activate()
            }
            tracer.deactivate()
        }
    }

    private fun configureEager() {
        project.apply(AllOpenGradleSubplugin::class.java)

        // Source sets need to be created early
        properties.test.apply {
            withIntegrationTests(true)
            withFunctionalTests(true)
        }

        project.apply(AllOpenGradleSubplugin::class.java)
        project.configure<AllOpenExtension> {
            annotation("org.openjdk.jmh.annotations.State")
        }

        blueprints.benchmarks.doCreateSourceSets()
        blueprints.test.doCreateTasks()
    }

    @SuppressWarnings("LongMethod")
    private fun configure() {
        val compatExtension = project.extensions.getByType(KradleCompatExtension::class.java)
        extension.apply {
            general.configureOnly {
                bootstrap.enable()
                git.enable()
                projectProperties.enable()
                buildProperties.enable()
            }

            jvm.configureOnly {
                targetJvm.set(compatExtension.targetJvm.orNull)
                kotlin {
                    kotlinxCoroutinesVersion.set(compatExtension.kotlinxCoroutinesVersion.orNull)
                    lint {
                        ktlint {
                            version.set(compatExtension.ktlintVersion.orNull)
                            rules {
                                disable("no-wildcard-imports")
                            }
                        }
                    }
                    codeAnalysis {
                        detektConfigFile.set(compatExtension.detektConfigFile.orNull)
                        detektVersion.set(compatExtension.detektVersion.orNull)
                    }
                    test {
                        useMockk.set(compatExtension.tests.mockkVersion.orNull)
                        useKotest.set(compatExtension.tests.kotestVersion.orNull)
                    }
                }

                when (type) {
                    ProjectType.APPLICATION -> application {
                        mainClass(compatExtension.mainClass)
                    }
                    ProjectType.LIBRARY -> library.enable()
                }

                dependencyUpdates.enable()
                vulnerabilityScan.enable()
                lint.enable()
                codeAnalysis.enable()
                if (type == ProjectType.APPLICATION) {
                    developmentMode.enable()
                }

                test {
                    prettyPrint(true)
                    if (compatExtension.tests.junitJupiterVersion.isPresent) {
                        junitJupiter {
                            version(compatExtension.tests.junitJupiterVersion.get())
                        }
                    } else {
                        junitJupiter.disable()
                    }
                    if (compatExtension.tests.jacocoVersion.isPresent) {
                        jacoco.enable {
                            version(compatExtension.tests.jacocoVersion.get())
                        }
                    }
                }
                codeCoverage {
                    kover.disable()
                }
                benchmark {
                    jmhVersion.set(compatExtension.jmhVersion.orNull)
                }
                `package` {
                    uberJar {
                        minimize.set(compatExtension.uberJar.minimize.get())
                    }
                }

                if (type == ProjectType.APPLICATION) {
                    docker.enable {
                        baseImage.set(compatExtension.image.baseImage.orNull)
                        compatExtension.image.ports.get().forEach { ports.add(it) }
                        withJvmKill.set(compatExtension.image.jvmKillVersion.orNull)
                        withAppSh.set(compatExtension.image.withAppSh.get())
                        javaOpts.set(compatExtension.image.javaOpts.orNull)
                    }
                }
                documentation.enable()
            }
        }
    }
}
