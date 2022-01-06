package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase
import org.gradle.api.Project

class JavaLibraryPreset(project: Project) : Preset(project) {

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
                library.enable()
                dependencyUpdates.enable()
                vulnerabilityScan.enable()
                lint.enable()
                codeAnalysis.enable()

                test {
                    prettyPrint(true)
                    integrationTests()
                    functionalTests()
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
