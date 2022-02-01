package net.bnb1.kradle

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import io.kotest.core.spec.style.FunSpec
import net.bnb1.kradle.config.KradleContext

class KradleContextSpec : FunSpec({

    val classes = ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .withImportOption { it.contains("Test") }
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .importPackages("net.bnb1.kradle")

    test("Limit access to Kradle context") {
        var rule = ArchRuleDefinition
            .classes().that().areAssignableTo(KradleContext::class.java)
            .should().onlyHaveDependentClassesThat().resideInAnyPackage(
                "net.bnb1.kradle.plugins",
                "net.bnb1.kradle.v1"
            )

        rule.check(classes)
    }
})
