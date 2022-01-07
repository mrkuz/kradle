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

    private val _kotlinJvmApplication by context { KotlinJvmApplicationPreset(presetLock) }
    val kotlinJvmApplication = PresetDsl(_kotlinJvmApplication, project)

    private val _kotlinJvmLibrary by context { KotlinJvmLibraryPreset(presetLock) }
    val kotlinJvmLibrary = PresetDsl(_kotlinJvmLibrary, project)

    private val _javaApplication by context { JavaApplicationPreset(presetLock) }
    val javaApplication = PresetDsl(_javaApplication, project)

    private val _javaLibrary by context { JavaLibraryPreset(presetLock) }
    val javaLibrary = PresetDsl(_javaLibrary, project)
}
