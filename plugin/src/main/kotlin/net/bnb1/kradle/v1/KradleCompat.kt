package net.bnb1.kradle.v1

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.apply
import net.bnb1.kradle.features.jvm.BenchmarksBlueprint
import net.bnb1.kradle.features.jvm.TestBlueprint
import net.bnb1.kradle.features.jvm.TestProperties
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

class KradleCompat(private val project: Project, private val type: ProjectType) {

    enum class ProjectType {
        APPLICATION, LIBRARY
    }

    private val extension = KradleExtension(project)

    fun activate() {
        configureEager()
        project.afterEvaluate { configure() }
    }

    private fun configureEager() {
        project.apply(AllOpenGradleSubplugin::class.java)

        // Source sets need to be created early
        project.propertiesRegistry.get(TestProperties::class).apply {
            withIntegrationTests(true)
            withFunctionalTests(true)
        }
        BenchmarksBlueprint(project).createSourceSets()
        TestBlueprint(project).createTasks()
    }

    @SuppressWarnings("LongMethod")
    private fun configure() {
        val compatExtension = project.extensions.getByType(KradleCompatExtension::class.java)
        with(extension) {
            general {
                bootstrap.enable()
                git.enable()
                projectProperties.enable()
                buildProperties.enable()
            }

            jvm {
                targetJvm.bind(compatExtension.targetJvm)
                kotlin {
                    kotlinxCoroutinesVersion.bind(compatExtension.kotlinxCoroutinesVersion)
                    lint {
                        ktlintVersion.bind(compatExtension.ktlintVersion)
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
