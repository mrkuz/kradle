package net.bnb1.kradle

import org.gradle.api.Project
import javax.inject.Inject

open class KradleExtension @Inject constructor(project: Project) : KradleExtensionBase(project) {

    val kotlinJvmApplication = PresetDsl.Builder(project)
        .preset { KotlinJvmApplicationPreset(it) }
        .build()
    val kotlinJvmLibrary = PresetDsl.Builder(project)
        .preset { KotlinJvmLibraryPreset(it) }
        .build()
    val javaApplication = PresetDsl.Builder(project)
        .preset { JavaApplicationPreset(it) }
        .build()
    val javaLibrary = PresetDsl.Builder(project)
        .preset { JavaLibraryPreset(it) }
        .build()
}
