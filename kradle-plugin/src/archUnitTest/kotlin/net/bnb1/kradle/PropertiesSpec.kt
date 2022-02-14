package net.bnb1.kradle

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import io.kotest.core.spec.style.FunSpec
import net.bnb1.kradle.core.Properties

class PropertiesSpec : FunSpec({

    val classes = ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .withImportOption { it.contains("Test") }
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .importPackages("net.bnb1.kradle")

    test("Properties naming") {
        var rule = ArchRuleDefinition
            .classes().that().areAssignableTo(Properties::class.java)
            .should().haveSimpleNameEndingWith("Properties")

        rule.check(classes)
    }
})
