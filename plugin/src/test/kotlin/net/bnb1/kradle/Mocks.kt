package net.bnb1.kradle

import io.mockk.every
import io.mockk.mockk
import net.bnb1.kradle.features.FeatureRegistry
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

object Mocks {

    fun project() = mockk<Project>(relaxed = true) {
        every { extra.get("featureRegistry") } returns FeatureRegistry()
    }
}
