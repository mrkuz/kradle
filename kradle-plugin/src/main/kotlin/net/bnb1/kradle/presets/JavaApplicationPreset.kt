package net.bnb1.kradle.presets

import net.bnb1.kradle.config.dsl.KradleExtensionDsl
import net.bnb1.kradle.core.Preset
import java.util.concurrent.atomic.AtomicBoolean

class JavaApplicationPreset(lock: AtomicBoolean) : Preset<KradleExtensionDsl>(lock) {

    override fun doConfigure(target: KradleExtensionDsl) {
        target.apply {
            general.configureOnly {
                bootstrap.enable()
                git.enable()
                projectProperties.enable()
                buildProperties.enable()
            }

            jvm.configureOnly {
                java {
                    withLombok()
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
                    withIntegrationTests()
                    withFunctionalTests()
                }
                codeCoverage.enable()

                benchmark.enable()
                packaging.enable()
                docker {
                    withJvmKill()
                }
                documentation.enable()
            }
        }
    }

    override fun doActivate(target: KradleExtensionDsl) {
        target.run {
            general.tryActivate()
            jvm.tryActivate()
        }
    }
}
