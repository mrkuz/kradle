package net.bnb1.kradle.features

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import net.bnb1.kradle.features.jvm.DependencyUpdatesBlueprint

class DependencyUpdatesBlueprintTests : FunSpec({

    test("Exclude alphas, RCs and milestones") {

        val filter = DependencyUpdatesBlueprint.VersionFilter

        filter.reject("1.0.0-alpha").shouldBeTrue()
        filter.reject("1.0.0.alpha").shouldBeTrue()
        filter.reject("1.0.0-alpha1").shouldBeTrue()
        filter.reject("1.0.0.alpha1").shouldBeTrue()
        filter.reject("1.0.0-alpha-1").shouldBeTrue()
        filter.reject("1.0.0.alpha-1").shouldBeTrue()
        filter.reject("1.0.0-alpha.1").shouldBeTrue()
        filter.reject("1.0.0.alpha.1").shouldBeTrue()
        filter.reject("1.0.0-RC").shouldBeTrue()
        filter.reject("1.0.0.RC").shouldBeTrue()
        filter.reject("1.0.0-RC1").shouldBeTrue()
        filter.reject("1.0.0.RC1").shouldBeTrue()
        filter.reject("1.0.0-RC-1").shouldBeTrue()
        filter.reject("1.0.0.RC-1").shouldBeTrue()
        filter.reject("1.0.0-RC.1").shouldBeTrue()
        filter.reject("1.0.0.RC.1").shouldBeTrue()
        filter.reject("1.0.0-M1").shouldBeTrue()
        filter.reject("1.0.0.M1").shouldBeTrue()
        filter.reject("1.0.0-M-1").shouldBeTrue()
        filter.reject("1.0.0.M-1").shouldBeTrue()
        filter.reject("1.0.0-M.1").shouldBeTrue()
        filter.reject("1.0.0.M.1").shouldBeTrue()
    }
})
