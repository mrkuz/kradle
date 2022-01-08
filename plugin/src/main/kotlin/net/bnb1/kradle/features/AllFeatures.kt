package net.bnb1.kradle.features

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.features.general.BootstrapFeature
import net.bnb1.kradle.features.general.BuildPropertiesFeature
import net.bnb1.kradle.features.general.GitFeature
import net.bnb1.kradle.features.general.ProjectPropertiesFeature
import net.bnb1.kradle.features.jvm.ApplicationFeature
import net.bnb1.kradle.features.jvm.BenchmarkFeature
import net.bnb1.kradle.features.jvm.CodeAnalysisFeature
import net.bnb1.kradle.features.jvm.DependencyUpdatesFeature
import net.bnb1.kradle.features.jvm.DevelopmentModeFeature
import net.bnb1.kradle.features.jvm.DockerFeature
import net.bnb1.kradle.features.jvm.DocumentationFeature
import net.bnb1.kradle.features.jvm.JavaFeature
import net.bnb1.kradle.features.jvm.KotlinFeature
import net.bnb1.kradle.features.jvm.LibraryFeature
import net.bnb1.kradle.features.jvm.LintFeature
import net.bnb1.kradle.features.jvm.PackageFeature
import net.bnb1.kradle.features.jvm.TestFeature
import net.bnb1.kradle.features.jvm.VulnerabilityScanFeature

class AllFeatures(context: KradleContext) {

    // General
    val bootstrap by context { BootstrapFeature() }
    val git by context { GitFeature() }
    val projectProperties by context { ProjectPropertiesFeature() }
    val buildProperties by context { BuildPropertiesFeature() }

    // JVM
    val kotlin by context { KotlinFeature() }
    val java by context { JavaFeature() }
    val application by context { ApplicationFeature() }
    val library by context { LibraryFeature() }
    val dependencyUpdates by context { DependencyUpdatesFeature() }
    val vulnerabilityScan by context { VulnerabilityScanFeature() }
    val lint by context { LintFeature() }
    val codeAnalysis by context { CodeAnalysisFeature() }
    val developmentMode by context { DevelopmentModeFeature() }
    val test by context { TestFeature() }
    val benchmark by context { BenchmarkFeature() }
    val packaging by context { PackageFeature() }
    val docker by context { DockerFeature() }
    val documentation by context { DocumentationFeature() }
}
