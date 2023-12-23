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
            }

            jvm.configureOnly {
                kotlin.enable()
                library.enable()
                dependencies.enable()
                lint.enable()
                codeAnalysis.enable()
                test.enable()
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
