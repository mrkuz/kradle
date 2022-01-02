package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase
import org.gradle.api.Project

class JavaApplicationPreset(project: Project) : Preset(project) {

    override fun onConfigure(extension: KradleExtensionBase) {
        extension.apply {
            general.configureOnly {
                bootstrap.enable()
                git.enable()
                projectProperties.enable()
                buildProperties.enable()
            }

            jvm.configureOnly {
                java {
                    codeAnalysis {
                        spotBugs {
                            useFbContrib()
                            useFindSecBugs()
                        }
                    }
                }
                dependencyUpdates.enable()
                vulnerabilityScan.enable()
                lint.enable()
                codeAnalysis.enable()
                developmentMode.enable()

                test {
                    prettyPrint(true)
                    withIntegrationTests(true)
                    withFunctionalTests(true)
                    withJunitJupiter()
                    withJacoco()
                }

                benchmark.enable()
                packaging.enable()
                docker {
                    withJvmKill()
                }
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
