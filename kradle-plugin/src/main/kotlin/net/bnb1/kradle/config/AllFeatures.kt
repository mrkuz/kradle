package net.bnb1.kradle.config

import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.support.Registry

class AllFeatures(registry: Registry) {

    // General
    var bootstrap = registry.create("bootstrap") { Feature(it, "bootstrap") }
    val git = registry("git") { Feature(it) }
    val projectProperties = registry("projectProperties") { Feature(it) }
    val buildProperties = registry("buildProperties") { Feature(it) }

    // JVM
    val kotlin = registry("kotlin") { Feature(it) }
    val java = registry("java") { Feature(it) }
    val application = registry("application") { Feature(it) }
    val library = registry("library") { Feature(it) }
    val dependencyUpdates = registry("dependencyUpdates") { Feature(it) }
    val vulnerabilityScan = registry("vulnerabilityScan") { Feature(it) }
    val lint = registry("lint") { Feature(it, "lint") }
    val codeAnalysis = registry("codeAnalysis") { Feature(it, "analyzeCode") }
    val developmentMode = registry("developmentMode") { Feature(it) }
    val test = registry("test") { Feature(it) }
    val codeCoverage = registry("codeCoverage") { Feature(it, "analyzeTestCoverage") }
    val benchmark = registry("benchmark") { Feature(it) }
    val packaging = registry("packaging") { Feature(it) }
    val docker = registry("docker") { Feature(it) }
    val documentation = registry("documentation") { Feature(it) }

    // Sub-features
    val checkstyle = registry("checkstyle") { Feature(it) }
    val pmd = registry("pmd") { Feature(it) }
    val spotBugs = registry("spotBugs") { Feature(it) }
    val ktlint = registry("ktlint") { Feature(it) }
    val detekt = registry("detekt") { Feature(it) }
    val jacoco = registry("jacoco") { Feature(it) }
    val kover = registry("kover") { Feature(it) }
    val junitJupiter = registry("junitJupiter") { Feature(it) }
}
