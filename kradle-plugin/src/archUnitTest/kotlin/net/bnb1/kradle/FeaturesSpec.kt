package net.bnb1.kradle

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import io.kotest.core.spec.style.FunSpec
import net.bnb1.kradle.core.Feature

class FeaturesSpec : FunSpec({

    val classes = ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .withImportOption { it.contains("Test") }
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .importPackages("net.bnb1.kradle")

    test("Feature naming") {
        var rule = ArchRuleDefinition
            .classes().that().areAssignableTo(Feature::class.java)
            .should().haveSimpleNameEndingWith("Feature")

        rule.check(classes)
    }
})
