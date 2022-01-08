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
import net.bnb1.kradle.features.jvm.PackageProperties
import net.bnb1.kradle.features.jvm.PackageUberJarProperties
import net.bnb1.kradle.features.jvm.PmdProperties
import net.bnb1.kradle.features.jvm.SpotBugsProperties
import net.bnb1.kradle.features.jvm.TestProperties

class AllProperties(context: KradleContext) {

    val jvm by context { JvmProperties() }

    val ktlint by context { KtlintProperties() }
    val detekt by context { DetektProperties() }
    val kotlinTest by context { KotlinTestProperties() }
    val kotlin by context { KotlinProperties() }

    val checkstyle by context { CheckstyleProperties() }
    val pmd by context { PmdProperties() }
    val spotBugs by context { SpotBugsProperties() }
    val java by context { JavaProperties() }

    val application by context { ApplicationProperties() }
    val lint by context { LintProperties() }
    val codeAnalysis by context { CodeAnalysisProperties() }
    val test by context { TestProperties() }
    val benchmark by context { BenchmarkProperties() }

    val uberJar by context { PackageUberJarProperties() }
    val packaging by context { PackageProperties(context) }

    val docker by context { DockerProperties() }
}
