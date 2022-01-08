package net.bnb1.kradle

import io.mockk.mockk
import org.gradle.api.Project

object Mocks {

    fun project() = mockk<Project>(relaxed = true)
}
