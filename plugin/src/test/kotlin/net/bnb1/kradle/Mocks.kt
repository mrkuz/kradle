package net.bnb1.kradle

import io.mockk.every
import io.mockk.mockk
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

object Mocks {

    fun project() = mockk<Project>(relaxed = true) {
        every { extra.get("tracer") } returns Tracer()
        every { extra.get("context") } returns KradleContext()
    }
}
