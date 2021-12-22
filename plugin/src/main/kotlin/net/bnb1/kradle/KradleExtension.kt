package net.bnb1.kradle

import net.bnb1.kradle.presets.ConfigurablePresetImpl
import net.bnb1.kradle.presets.KotlinJvmApplicationPreset
import net.bnb1.kradle.presets.KotlinJvmLibraryPreset
import org.gradle.api.Project
import javax.inject.Inject

open class KradleExtension @Inject constructor(project: Project) : KradleExtensionBase(project) {

    val kotlinJvmApplication = ConfigurablePresetImpl(KotlinJvmApplicationPreset(project), project)
        .register(project)
        .asInterface()
    val kotlinJvmLibrary = ConfigurablePresetImpl(KotlinJvmLibraryPreset(project), project)
        .register(project)
        .asInterface()
}
