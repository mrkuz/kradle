package net.bnb1.kradle.config

import net.bnb1.kradle.blueprints.general.BootstrapBlueprint
import net.bnb1.kradle.blueprints.general.BuildPropertiesBlueprint
import net.bnb1.kradle.blueprints.general.GitBlueprint
import net.bnb1.kradle.blueprints.general.ProjectPropertiesBlueprint
import net.bnb1.kradle.blueprints.general.ScriptsBlueprint
import net.bnb1.kradle.blueprints.jvm.AllOpenBlueprint
import net.bnb1.kradle.blueprints.jvm.ApplicationBlueprint
import net.bnb1.kradle.blueprints.jvm.BenchmarksBlueprint
import net.bnb1.kradle.blueprints.jvm.CheckstyleBlueprint
import net.bnb1.kradle.blueprints.jvm.CodeAnalysisBlueprint
import net.bnb1.kradle.blueprints.jvm.CodeCoverageBlueprint
import net.bnb1.kradle.blueprints.jvm.DependenciesBlueprint
import net.bnb1.kradle.blueprints.jvm.DependencyUpdatesBlueprint
import net.bnb1.kradle.blueprints.jvm.DetektBlueprint
import net.bnb1.kradle.blueprints.jvm.DevelopmentModeBlueprint
import net.bnb1.kradle.blueprints.jvm.DokkaBlueprint
import net.bnb1.kradle.blueprints.jvm.JacocoBlueprint
import net.bnb1.kradle.blueprints.jvm.JavaAppBootstrapBlueprint
import net.bnb1.kradle.blueprints.jvm.JavaBlueprint
import net.bnb1.kradle.blueprints.jvm.JavaLibBootstrapBlueprint
import net.bnb1.kradle.blueprints.jvm.JibBlueprint
import net.bnb1.kradle.blueprints.jvm.JunitJupiterBlueprint
import net.bnb1.kradle.blueprints.jvm.KotlinAppBootstrapBlueprint
import net.bnb1.kradle.blueprints.jvm.KotlinBlueprint
import net.bnb1.kradle.blueprints.jvm.KotlinLibBootstrapBlueprint
import net.bnb1.kradle.blueprints.jvm.KotlinTestBlueprint
import net.bnb1.kradle.blueprints.jvm.KoverBlueprint
import net.bnb1.kradle.blueprints.jvm.KtlintBlueprint
import net.bnb1.kradle.blueprints.jvm.LibraryBlueprint
import net.bnb1.kradle.blueprints.jvm.LintBlueprint
import net.bnb1.kradle.blueprints.jvm.MavenPublishBlueprint
import net.bnb1.kradle.blueprints.jvm.OwaspDependencyCheckBlueprint
import net.bnb1.kradle.blueprints.jvm.PackageApplicationBlueprint
import net.bnb1.kradle.blueprints.jvm.PackagingBlueprint
import net.bnb1.kradle.blueprints.jvm.PmdBlueprint
import net.bnb1.kradle.blueprints.jvm.ShadowBlueprint
import net.bnb1.kradle.blueprints.jvm.SpotBugsBlueprint
import net.bnb1.kradle.blueprints.jvm.TestBlueprint
import net.bnb1.kradle.inject
import net.bnb1.kradle.support.Registry
import org.gradle.api.Project

class AllBlueprints(registry: Registry, properties: AllProperties, project: Project) {

    // General
    val bootstrap = registry { BootstrapBlueprint(project) }
    val git = registry { GitBlueprint(project) }
    val projectProperties = registry { ProjectPropertiesBlueprint(project) }
    val buildProperties = registry { BuildPropertiesBlueprint(project) }
    val scripts = registry {
        ScriptsBlueprint(project).inject {
            scriptsProperties = properties.scripts
        }
    }

    // JVM
    val java = registry {
        JavaBlueprint(project).inject {
            javaProperties = properties.java
            jvmProperties = properties.jvm
        }
    }
    val kotlin = registry {
        KotlinBlueprint(project).inject {
            kotlinProperties = properties.kotlin
            jvmProperties = properties.jvm
        }
    }
    val allOpen = registry { AllOpenBlueprint(project) }
    val application = registry {
        ApplicationBlueprint(project).inject {
            applicationProperties = properties.application
            javaProperties = properties.java
        }
    }
    val library = registry { LibraryBlueprint(project) }
    val mavenPublish = registry { MavenPublishBlueprint(project) }
    val dependencyUpdates = registry { DependencyUpdatesBlueprint(project) }
    val dependencies = registry {
        DependenciesBlueprint(project).inject {
            dependenciesProperties = properties.dependencies
        }
    }
    val owaspDependencyCheck = registry { OwaspDependencyCheckBlueprint(project) }
    val lint = registry { LintBlueprint(project) }
    val codeAnalysis = registry { CodeAnalysisBlueprint(project) }
    val developmentMode = registry {
        DevelopmentModeBlueprint(project).inject {
            applicationProperties = properties.application
            javaProperties = properties.java
        }
    }

    val test = registry {
        TestBlueprint(project).inject {
            testProperties = properties.test
            javaProperties = properties.java
        }
    }
    val codeCoverage = registry { CodeCoverageBlueprint(project) }
    val junitJupiter = registry {
        JunitJupiterBlueprint(project).inject {
            junitJupiterProperties = properties.junitJupiter
        }
    }
    val jacoco = registry {
        JacocoBlueprint(project).inject {
            jacocoProperties = properties.jacoco
        }
    }
    val kover = registry {
        KoverBlueprint(project).inject {
            koverProperties = properties.kover
        }
    }
    val benchmarks = registry {
        BenchmarksBlueprint(project).inject {
            jmhProperties = properties.jmh
            javaProperties = properties.java
        }
    }
    val packaging = registry { PackagingBlueprint(project) }
    val packageApplication = registry {
        PackageApplicationBlueprint(project).inject {
            applicationProperties = properties.application
        }
    }
    val shadow = registry {
        ShadowBlueprint(project).inject {
            shadowProperties = properties.shadow
        }
    }
    val dokka = registry { DokkaBlueprint(project) }
    val jib = registry {
        JibBlueprint(project).inject {
            dockerProperties = properties.docker
            applicationProperties = properties.application
        }
    }
    val javaAppBootstrap = registry {
        JavaAppBootstrapBlueprint(project).inject {
            applicationProperties = properties.application
        }
    }
    val javaLibBootstrap = registry { JavaLibBootstrapBlueprint(project) }
    val pmd = registry {
        PmdBlueprint(project).inject {
            pmdProperties = properties.pmd
            codeAnalysisProperties = properties.codeAnalysis
        }
    }
    val spotBugs = registry {
        SpotBugsBlueprint(project).inject {
            spotBugsProperties = properties.spotBugs
            codeAnalysisProperties = properties.codeAnalysis
        }
    }
    val checkstyle = registry {
        CheckstyleBlueprint(project).inject {
            checkstyleProperties = properties.checkstyle
            lintProperties = properties.lint
        }
    }
    val kotlinAppBootstrap = registry {
        KotlinAppBootstrapBlueprint(project).inject {
            applicationProperties = properties.application
        }
    }
    val kotlinLibBootstrap = registry { KotlinLibBootstrapBlueprint(project) }
    val detekt = registry {
        DetektBlueprint(project).inject {
            detektProperties = properties.detekt
            codeAnalysisProperties = properties.codeAnalysis
        }
    }
    val ktlint = registry {
        KtlintBlueprint(project).inject {
            ktlintProperties = properties.ktlint
            lintProperties = properties.lint
        }
    }
    val kotlinTest = registry {
        KotlinTestBlueprint(project).inject {
            kotlinTestProperties = properties.kotlinTest
        }
    }
}
