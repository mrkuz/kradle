package net.bnb1.kradle.config

import net.bnb1.kradle.features.jvm.ApplicationProperties
import net.bnb1.kradle.features.jvm.BenchmarkProperties
import net.bnb1.kradle.features.jvm.CheckstyleProperties
import net.bnb1.kradle.features.jvm.CodeAnalysisProperties
import net.bnb1.kradle.features.jvm.DetektProperties
import net.bnb1.kradle.features.jvm.DockerProperties
import net.bnb1.kradle.features.jvm.JavaProperties
import net.bnb1.kradle.features.jvm.JvmProperties
import net.bnb1.kradle.features.jvm.KotlinProperties
import net.bnb1.kradle.features.jvm.KotlinTestProperties
import net.bnb1.kradle.features.jvm.KtlintProperties
import net.bnb1.kradle.features.jvm.LintProperties
import net.bnb1.kradle.features.jvm.PmdProperties
import net.bnb1.kradle.features.jvm.ShadowProperties
import net.bnb1.kradle.features.jvm.SpotBugsProperties
import net.bnb1.kradle.features.jvm.TestProperties
import net.bnb1.kradle.support.Registry

class AllProperties(registry: Registry) {

    val jvm = registry { JvmProperties() }

    val ktlint = registry { KtlintProperties() }
    val detekt = registry { DetektProperties() }
    val kotlinTest = registry { KotlinTestProperties() }
    val kotlin = registry { KotlinProperties() }

    val checkstyle = registry { CheckstyleProperties() }
    val pmd = registry { PmdProperties() }
    val spotBugs = registry { SpotBugsProperties() }
    val java = registry { JavaProperties() }

    val application = registry { ApplicationProperties() }
    val lint = registry { LintProperties() }
    val codeAnalysis = registry { CodeAnalysisProperties() }
    val test = registry { TestProperties() }
    val benchmark = registry { BenchmarkProperties() }

    val uberJar = registry { ShadowProperties() }

    val docker = registry { DockerProperties() }
}
