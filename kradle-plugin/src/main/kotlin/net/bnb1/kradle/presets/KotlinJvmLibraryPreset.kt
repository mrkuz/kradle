package net.bnb1.kradle.presets

import net.bnb1.kradle.config.dsl.KradleExtensionDsl
import net.bnb1.kradle.core.Preset
import java.util.concurrent.atomic.AtomicBoolean

class KotlinJvmLibraryPreset(lock: AtomicBoolean) : Preset<KradleExtensionDsl>(lock) {

    override fun doConfigure(target: KradleExtensionDsl) {
        target.apply {
            general.configureOnly {
                bootstrap.enable()
                git.enable()
                projectProperties.enable()
                buildProperties.enable()
            }

            jvm.configureOnly {
                kotlin {
                    useCoroutines()
                    lint {
                        ktlint {
                            rules {
                                disable("no-wildcard-imports")
                            }
                        }
                    }
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
                    withIntegrationTests()
                    withFunctionalTests()
                }
                codeCoverage.enable()

                benchmark.enable()
                packaging.enable()
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
