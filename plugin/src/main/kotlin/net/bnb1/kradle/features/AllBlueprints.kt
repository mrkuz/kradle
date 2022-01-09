package net.bnb1.kradle.features

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.features.general.BootstrapBlueprint
import net.bnb1.kradle.features.general.BuildPropertiesBlueprint
import net.bnb1.kradle.features.general.GitBlueprint
import net.bnb1.kradle.features.general.ProjectPropertiesBlueprint
import net.bnb1.kradle.features.jvm.AllOpenBlueprint
import net.bnb1.kradle.features.jvm.ApplicationBlueprint
import net.bnb1.kradle.features.jvm.BenchmarksBlueprint
import net.bnb1.kradle.features.jvm.CheckstyleBlueprint
import net.bnb1.kradle.features.jvm.CodeAnalysisBlueprint
import net.bnb1.kradle.features.jvm.DependencyUpdatesBlueprint
import net.bnb1.kradle.features.jvm.DetektBlueprint
import net.bnb1.kradle.features.jvm.DevelopmentModeBlueprint
import net.bnb1.kradle.features.jvm.DokkaBlueprint
import net.bnb1.kradle.features.jvm.JacocoBlueprint
import net.bnb1.kradle.features.jvm.JavaAppBootstrapBlueprint
import net.bnb1.kradle.features.jvm.JavaBlueprint
import net.bnb1.kradle.features.jvm.JavaLibBootstrapBlueprint
import net.bnb1.kradle.features.jvm.JibBlueprint
import net.bnb1.kradle.features.jvm.KotlinAppBootstrapBlueprint
import net.bnb1.kradle.features.jvm.KotlinBlueprint
import net.bnb1.kradle.features.jvm.KotlinLibBootstrapBlueprint
import net.bnb1.kradle.features.jvm.KotlinTestBlueprint
import net.bnb1.kradle.features.jvm.KtlintBlueprint
import net.bnb1.kradle.features.jvm.LibraryBlueprint
import net.bnb1.kradle.features.jvm.LintBlueprint
import net.bnb1.kradle.features.jvm.MavenPublishBlueprint
import net.bnb1.kradle.features.jvm.OwaspDependencyCheckBlueprint
import net.bnb1.kradle.features.jvm.PackageApplicationBlueprint
import net.bnb1.kradle.features.jvm.PackagingBlueprint
import net.bnb1.kradle.features.jvm.PmdBlueprint
import net.bnb1.kradle.features.jvm.ShadowBlueprint
import net.bnb1.kradle.features.jvm.SpotBugsBlueprint
import net.bnb1.kradle.features.jvm.TestBlueprint
import net.bnb1.kradle.inject
import org.gradle.api.Project

class AllBlueprints(context: KradleContext, properties: AllProperties, project: Project) {

    // General
    val bootstrap = context { BootstrapBlueprint(project) }
    val git = context { GitBlueprint(project) }
    val projectProperties = context { ProjectPropertiesBlueprint(project) }
    val buildProperties = context { BuildPropertiesBlueprint(project) }

    // JVM
    val java = context {
        JavaBlueprint(project).inject {
            javaProperties = properties.java
            jvmProperties = properties.jvm
        }
    }
    val kotlin = context {
        KotlinBlueprint(project).inject {
            kotlinProperties = properties.kotlin
            jvmProperties = properties.jvm
        }
    }
    val allOpen = context { AllOpenBlueprint(project) }
    val application = context {
        ApplicationBlueprint(project).inject {
            applicationProperties = properties.application
            javaProperties = properties.java
        }
    }
    val library = context { LibraryBlueprint(project) }
    val mavenPublish = context { MavenPublishBlueprint(project) }
    val dependencyUpdates = context { DependencyUpdatesBlueprint(project) }
    val owaspDependencyCheck = context { OwaspDependencyCheckBlueprint(project) }
    val lint = context { LintBlueprint(project) }
    val codeAnalysis = context { CodeAnalysisBlueprint(project) }
    val developmentMode = context {
        DevelopmentModeBlueprint(project).inject {
            applicationProperties = properties.application
            javaProperties = properties.java
        }
    }

    val test = context {
        TestBlueprint(project).inject {
            testProperties = properties.test
            javaProperties = properties.java
        }
    }
    val jacoco = context {
        JacocoBlueprint(project).inject {
            testProperties = properties.test
        }
    }
    val benchmarks = context {
        BenchmarksBlueprint(project).inject {
            benchmarkProperties = properties.benchmark
            javaProperties = properties.java
        }
    }
    val packaging = context { PackagingBlueprint(project) }
    val packageApplication = context {
        PackageApplicationBlueprint(project).inject {
            applicationProperties = properties.application
        }
    }
    val shadow = context {
        ShadowBlueprint(project).inject {
            uberJarProperties = properties.uberJar
        }
    }
    val dokka = context { DokkaBlueprint(project) }
    val jib = context {
        JibBlueprint(project).inject {
            dockerProperties = properties.docker
            applicationProperties = properties.application
        }
    }
    val javaAppBootstrap = context {
        JavaAppBootstrapBlueprint(project).inject {
            applicationProperties = properties.application
        }
    }
    val javaLibBootstrap = context { JavaLibBootstrapBlueprint(project) }
    val pmd = context {
        PmdBlueprint(project).inject {
            pmdProperties = properties.pmd
            codeAnalysisProperties = properties.codeAnalysis
        }
    }
    val spotBugs = context {
        SpotBugsBlueprint(project).inject {
            spotBugsProperties = properties.spotBugs
            codeAnalysisProperties = properties.codeAnalysis
        }
    }
    val checkstyle = context {
        CheckstyleBlueprint(project).inject {
            checkstyleProperties = properties.checkstyle
            lintProperties = properties.lint
        }
    }
    val kotlinAppBootstrap = context {
        KotlinAppBootstrapBlueprint(project).inject {
            applicationProperties = properties.application
        }
    }
    val kotlinLibBootstrap = context { KotlinLibBootstrapBlueprint(project) }
    val detekt = context {
        DetektBlueprint(project).inject {
            detektProperties = properties.detekt
            codeAnalysisProperties = properties.codeAnalysis
        }
    }
    val ktlint = context {
        KtlintBlueprint(project).inject {
            ktlintProperties = properties.ktlint
            lintProperties = properties.lint
        }
    }
    val kotlinTest = context {
        KotlinTestBlueprint(project).inject {
            kotlinTestProperties = properties.kotlinTest
            testProperties = properties.test
        }
    }
}
