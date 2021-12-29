package net.bnb1.kradle

import net.bnb1.kradle.presets.KotlinJvmApplicationPreset
import net.bnb1.kradle.presets.KotlinJvmLibraryPreset
import net.bnb1.kradle.presets.PresetDsl
import org.gradle.api.Project
import javax.inject.Inject

open class KradleExtension @Inject constructor(project: Project) : KradleExtensionBase(project) {

    val kotlinJvmApplication = PresetDsl.Builder(project)
        .preset { KotlinJvmApplicationPreset(it) }
        .build()
    val kotlinJvmLibrary = PresetDsl.Builder(project)
        .preset { KotlinJvmLibraryPreset(it) }
        .build()
}
