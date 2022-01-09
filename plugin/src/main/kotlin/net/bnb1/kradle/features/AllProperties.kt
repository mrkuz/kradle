package net.bnb1.kradle.features

import net.bnb1.kradle.KradleContext
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

class AllProperties(context: KradleContext) {

    val jvm = context { JvmProperties() }

    val ktlint = context { KtlintProperties() }
    val detekt = context { DetektProperties() }
    val kotlinTest = context { KotlinTestProperties() }
    val kotlin = context { KotlinProperties() }

    val checkstyle = context { CheckstyleProperties() }
    val pmd = context { PmdProperties() }
    val spotBugs = context { SpotBugsProperties() }
    val java = context { JavaProperties() }

    val application = context { ApplicationProperties() }
    val lint = context { LintProperties() }
    val codeAnalysis = context { CodeAnalysisProperties() }
    val test = context { TestProperties() }
    val benchmark = context { BenchmarkProperties() }

    val uberJar = context { ShadowProperties() }

    val docker = context { DockerProperties() }
}
