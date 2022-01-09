package net.bnb1.kradle.features

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.core.Feature

class AllFeatures(context: KradleContext) {

    // General
    var bootstrap = context.create("bootstrap") { Feature(it) }
    val git = context("git") { Feature(it) }
    val projectProperties = context("projectProperties") { Feature(it) }
    val buildProperties = context("buildProperties") { Feature(it) }

    // JVM
    val kotlin = context("kotlin") { Feature(it) }
    val java = context("java") { Feature(it) }
    val application = context("application") { Feature(it) }
    val library = context("library") { Feature(it) }
    val dependencyUpdates = context("dependencyUpdates") { Feature(it) }
    val vulnerabilityScan = context("vulnerabilityScan") { Feature(it) }
    val lint = context("lint") { Feature(it) }
    val codeAnalysis = context("codeAnalysis") { Feature(it, "analyzeCode") }
    val developmentMode = context("developmentMode") { Feature(it) }
    val test = context("test") { Feature(it) }
    val benchmark = context("benchmark") { Feature(it) }
    val packaging = context("packaging") { Feature(it) }
    val docker = context("docker") { Feature(it) }
    val documentation = context("documentation") { Feature(it) }
}
