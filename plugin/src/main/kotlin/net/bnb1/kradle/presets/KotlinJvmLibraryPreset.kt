package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase
import org.gradle.api.Project

class KotlinJvmLibraryPreset(project: Project) : Preset(project) {

    override fun onConfigure(extension: KradleExtensionBase) {
        extension.apply {
            general.configureOnly {
                bootstrap.enable()
                git.enable()
                projectProperties.enable()
                buildProperties.enable()
            }

            jvm.configureOnly {
                kotlin {
                    useCoroutines()
                    test {
                        useKotest()
                        useMockk()
                    }
                }
                library.enable()
                dependencyUpdates.enable()
                vulnerabilityScan.enable()
                lint.enable()
                codeAnalysis.enable()

                test {
                    prettyPrint(true)
                    withIntegrationTests(true)
                    withFunctionalTests(true)
                    withJunitJupiter()
                    withJacoco()
                }

                benchmark.enable()
                packaging.enable()
                documentation.enable()
            }
        }
    }

    override fun onActivate(extension: KradleExtensionBase) {
        extension.run {
            general.tryActivate()
            jvm.tryActivate()
        }
    }
}
