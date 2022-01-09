package net.bnb1.kradle.presets

import net.bnb1.kradle.config.dsl.KradleExtensionDsl
import net.bnb1.kradle.core.Preset
import java.util.concurrent.atomic.AtomicBoolean

class JavaApplicationPreset(extension: KradleExtensionDsl, lock: AtomicBoolean) :
    Preset<KradleExtensionDsl>(extension, lock) {

    override fun doConfigure(extension: KradleExtensionDsl) {
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
                    integrationTests()
                    functionalTests()
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

    override fun doActivate(extension: KradleExtensionDsl) {
        extension.run {
            general.tryActivate()
            jvm.tryActivate()
        }
    }
}
