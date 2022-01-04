package net.bnb1.kradle.v1

import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.apply
import net.bnb1.kradle.features.jvm.BenchmarksBlueprint
import net.bnb1.kradle.features.jvm.TestBlueprint
import net.bnb1.kradle.features.jvm.TestProperties
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.tracer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

/**
 * Provides backwards-compatibility for Kradle v1.
 */
class KradleCompat(private val project: Project, private val type: ProjectType) {

    enum class ProjectType {
        APPLICATION, LIBRARY
    }

    private val extension = KradleExtensionBase(project)

    fun activate() {
        configureEager()
        project.afterEvaluate {
            configure()
            extension.run {
                general.activate()
                jvm.activate()
            }

            project.tracer.deactivate()
        }
    }

    private fun configureEager() {
        project.apply(AllOpenGradleSubplugin::class.java)

        // Source sets need to be created early
        project.propertiesRegistry.get(TestProperties::class).apply {
            withIntegrationTests(true)
            withFunctionalTests(true)
        }

        project.apply(AllOpenGradleSubplugin::class.java)
        project.configure<AllOpenExtension> {
            annotation("org.openjdk.jmh.annotations.State")
        }

        BenchmarksBlueprint(project).createSourceSets()
        TestBlueprint(project).createTasks()
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
                targetJvm.bind(compatExtension.targetJvm)
                kotlin {
                    kotlinxCoroutinesVersion.bind(compatExtension.kotlinxCoroutinesVersion)
                    lint {
                        ktlint {
                            version.bind(compatExtension.ktlintVersion)
                            rules {
                                disable("no-wildcard-imports")
                            }
                        }
                    }
                    codeAnalysis {
                        detektConfigFile.bind(compatExtension.detektConfigFile)
                        detektVersion.bind(compatExtension.detektVersion)
                    }
                    test {
                        mockkVersion.bind(compatExtension.tests.mockkVersion)
                        kotestVersion.bind(compatExtension.tests.kotestVersion)
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
                    junitJupiterVersion.bind(compatExtension.tests.junitJupiterVersion)
                    jacocoVersion.bind(compatExtension.tests.jacocoVersion)
                }
                benchmark {
                    jmhVersion.bind(compatExtension.jmhVersion)
                }
                `package` {
                    uberJar {
                        minimize.bind(compatExtension.uberJar.minimize)
                    }
                }

                if (type == ProjectType.APPLICATION) {
                    docker.enable {
                        baseImage.bind(compatExtension.image.baseImage)
                        ports.set(compatExtension.image.ports)
                        jvmKillVersion.bind(compatExtension.image.jvmKillVersion)
                        withAppSh.bind(compatExtension.image.withAppSh)
                        javaOpts.bind(compatExtension.image.javaOpts)
                    }
                }
                documentation.enable()
            }
        }
    }
}
