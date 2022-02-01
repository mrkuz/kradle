package net.bnb1.kradle.config

import net.bnb1.kradle.presets.JavaApplicationPreset
import net.bnb1.kradle.presets.JavaLibraryPreset
import net.bnb1.kradle.presets.KotlinJvmApplicationPreset
import net.bnb1.kradle.presets.KotlinJvmLibraryPreset
import net.bnb1.kradle.support.Registry
import java.util.concurrent.atomic.AtomicBoolean

class AllPresets(registry: Registry) {

    private val presetLock = AtomicBoolean()

    val kotlinJvmApplication = registry { KotlinJvmApplicationPreset(presetLock) }
    val kotlinJvmLibrary = registry { KotlinJvmLibraryPreset(presetLock) }
    val javaApplication = registry { JavaApplicationPreset(presetLock) }
    val javaLibrary = registry { JavaLibraryPreset(presetLock) }
}
