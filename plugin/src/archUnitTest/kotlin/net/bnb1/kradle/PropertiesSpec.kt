package net.bnb1.kradle

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaField
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import io.kotest.core.spec.style.FunSpec
import net.bnb1.kradle.dsl.ConfigurableSelf
import net.bnb1.kradle.dsl.Properties

class PropertiesSpec : FunSpec({

    val classes = ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .importPackages("net.bnb1.kradle")

    test("Properties naming") {
        var rule = ArchRuleDefinition
            .classes().that().areAssignableTo(Properties::class.java)
            .should().haveSimpleNameEndingWith("Properties")

        rule.check(classes)
    }

    test("Properties should only use DSL building blocks") {
        var rule = ArchRuleDefinition
            .classes().that().areAssignableTo(Properties::class.java)
            .and().resideOutsideOfPackage("net.bnb1.kradle.dsl")
            .should().onlyAccessFieldsThat(object : DescribedPredicate<JavaField>("DSL") {

                override fun apply(input: JavaField?): Boolean {
                    return input!!.rawType.packageName.startsWith("net.bnb1.kradle.dsl") ||
                        input.rawType.isAssignableTo(ConfigurableSelf::class.java)
                }
            })

        rule.check(classes)
    }
})
