package net.bnb1.kradle.presets

import net.bnb1.kradle.KradleExtensionBase
import java.util.concurrent.atomic.AtomicBoolean

class KotlinJvmLibraryPreset(extension: KradleExtensionBase, lock: AtomicBoolean) : Preset(extension, lock) {

    override fun doConfigure(extension: KradleExtensionBase) {
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

    override fun doActivate(extension: KradleExtensionBase) {
        extension.run {
            general.tryActivate()
            jvm.tryActivate()
        }
    }
}
