package net.bnb1.kradle

import net.bnb1.kradle.presets.KotlinJvmApplicationPreset
import net.bnb1.kradle.presets.KotlinJvmLibraryPreset
import net.bnb1.kradle.presets.PresetDslImpl
import org.gradle.api.Project
import javax.inject.Inject

open class KradleExtension @Inject constructor(project: Project) : KradleExtensionBase(project) {

    val kotlinJvmApplication = PresetDslImpl(KotlinJvmApplicationPreset(project), project)
        .register(project)
        .asInterface()
    val kotlinJvmLibrary = PresetDslImpl(KotlinJvmLibraryPreset(project), project)
        .register(project)
        .asInterface()
}
