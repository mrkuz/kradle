package net.bnb1.kradle.v1

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.apply
import net.bnb1.kradle.features.jvm.BenchmarksBlueprint
import net.bnb1.kradle.features.jvm.TestBlueprint
import net.bnb1.kradle.features.jvm.TestProperties
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

/**
 * Provides backwards-compatibility for Kradle v1.
 */
class KradleCompat(private val context: KradleContext, private val project: Project, private val type: ProjectType) {

    enum class ProjectType {
        APPLICATION, LIBRARY
    }

    private val extension = KradleExtensionBase(context, project)

    fun activate() {
        configureEager()
        project.afterEvaluate {
            configure()
            extension.run {
                general.activate()
                jvm.activate()
            }

            context.get<Tracer>().deactivate()
        }
    }

    private fun configureEager() {
        project.apply(AllOpenGradleSubplugin::class.java)

        // Source sets need to be created early
        context.get<TestProperties>().apply {
            withIntegrationTests(true)
            withFunctionalTests(true)
        }

        project.apply(AllOpenGradleSubplugin::class.java)
        project.configure<AllOpenExtension> {
            annotation("org.openjdk.jmh.annotations.State")
        }

        context.get<BenchmarksBlueprint>().createSourceSets()
        context.get<TestBlueprint>().createTasks()
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
                    withJunitJupiter.set(compatExtension.tests.junitJupiterVersion.orNull)
                    withJacoco.set(compatExtension.tests.jacocoVersion.orNull)
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
