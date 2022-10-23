package net.bnb1.kradle

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import io.kotest.core.spec.style.FunSpec

class CompatSpec : FunSpec({

    val classes = ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .withImportOption { !it.contains("Test") }
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .importPackages("net.bnb1.kradle")

    test("No global access to v1") {
        var rule = ArchRuleDefinition
            .noClasses().that().resideOutsideOfPackage("net.bnb1.kradle.v1")
            .should().dependOnClassesThat().resideInAPackage("net.bnb1.kradle.v1")
        rule.check(classes)
    }
})
