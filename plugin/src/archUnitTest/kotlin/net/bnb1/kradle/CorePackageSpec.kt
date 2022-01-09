package net.bnb1.kradle

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import io.kotest.core.spec.style.FunSpec
import net.bnb1.kradle.support.Tracer

class CorePackageSpec : FunSpec({

    val classes = ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .importPackages("net.bnb1.kradle")

    test("No global access from core") {
        var rule = ArchRuleDefinition
            .classes().that().resideOutsideOfPackage("net.bnb1.kradle.core..")
            .and().areNotAssignableTo(Tracer::class.java)
            .should().onlyHaveDependentClassesThat().resideOutsideOfPackage("net.bnb1.kradle.core..")

        rule.check(classes)
    }
})
