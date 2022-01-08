package net.bnb1.kradle

import net.bnb1.kradle.dsl.PresetDsl
import net.bnb1.kradle.presets.JavaApplicationPreset
import net.bnb1.kradle.presets.JavaLibraryPreset
import net.bnb1.kradle.presets.KotlinJvmApplicationPreset
import net.bnb1.kradle.presets.KotlinJvmLibraryPreset
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean

open class KradleExtension(context: KradleContext, project: Project) : KradleExtensionBase(context, project) {

    private val presetLock = AtomicBoolean()

    private val _kotlinJvmApplication by context { KotlinJvmApplicationPreset(this, presetLock) }
    val kotlinJvmApplication = PresetDsl(_kotlinJvmApplication)

    private val _kotlinJvmLibrary by context { KotlinJvmLibraryPreset(this, presetLock) }
    val kotlinJvmLibrary = PresetDsl(_kotlinJvmLibrary)

    private val _javaApplication by context { JavaApplicationPreset(this, presetLock) }
    val javaApplication = PresetDsl(_javaApplication)

    private val _javaLibrary by context { JavaLibraryPreset(this, presetLock) }
    val javaLibrary = PresetDsl(_javaLibrary)
}
