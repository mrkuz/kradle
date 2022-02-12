package net.bnb1.kradle.config

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.blueprints.general.HelmProperties
import net.bnb1.kradle.blueprints.general.ScriptsProperties
import net.bnb1.kradle.blueprints.jvm.ApplicationProperties
import net.bnb1.kradle.blueprints.jvm.CheckstyleProperties
import net.bnb1.kradle.blueprints.jvm.CodeAnalysisProperties
import net.bnb1.kradle.blueprints.jvm.DependenciesProperties
import net.bnb1.kradle.blueprints.jvm.DetektProperties
import net.bnb1.kradle.blueprints.jvm.DockerProperties
import net.bnb1.kradle.blueprints.jvm.JacocoProperties
import net.bnb1.kradle.blueprints.jvm.JavaProperties
import net.bnb1.kradle.blueprints.jvm.JmhProperties
import net.bnb1.kradle.blueprints.jvm.JunitJupiterProperties
import net.bnb1.kradle.blueprints.jvm.JvmProperties
import net.bnb1.kradle.blueprints.jvm.KotlinProperties
import net.bnb1.kradle.blueprints.jvm.KotlinTestProperties
import net.bnb1.kradle.blueprints.jvm.KoverProperties
import net.bnb1.kradle.blueprints.jvm.KtlintProperties
import net.bnb1.kradle.blueprints.jvm.LintProperties
import net.bnb1.kradle.blueprints.jvm.PmdProperties
import net.bnb1.kradle.blueprints.jvm.ShadowProperties
import net.bnb1.kradle.blueprints.jvm.SpotBugsProperties
import net.bnb1.kradle.blueprints.jvm.TestProperties
import net.bnb1.kradle.support.Registry

class AllProperties(registry: Registry) {

    // General
    val scripts = registry { ScriptsProperties() }
    val helm = registry { HelmProperties() }

    // JVM
    val jvm = registry { JvmProperties(Catalog.Versions.jvm) }

    val ktlint = registry { KtlintProperties(Catalog.Versions.ktlint) }
    val detekt = registry { DetektProperties(Catalog.Versions.detekt, "detekt-config.yml") }
    val kotlinTest = registry { KotlinTestProperties() }
    val kotlin = registry { KotlinProperties() }

    val checkstyle = registry { CheckstyleProperties(Catalog.Versions.checkstyle, "checkstyle.xml") }
    val pmd = registry { PmdProperties(Catalog.Versions.pmd) }
    val spotBugs = registry { SpotBugsProperties(Catalog.Versions.spotbugs) }
    val java = registry { JavaProperties() }

    val application = registry { ApplicationProperties() }
    val dependencies = registry { DependenciesProperties() }
    val lint = registry { LintProperties() }
    val codeAnalysis = registry { CodeAnalysisProperties() }
    val test = registry { TestProperties() }
    val junitJupiter = registry { JunitJupiterProperties(Catalog.Versions.junit) }
    val jacoco = registry { JacocoProperties(Catalog.Versions.jacoco) }
    val kover = registry { KoverProperties() }

    val jmh = registry { JmhProperties(Catalog.Versions.jmh) }
    val shadow = registry { ShadowProperties() }

    val docker = registry { DockerProperties("bellsoft/liberica-openjdk-alpine:${Catalog.Versions.jvm}") }
}
