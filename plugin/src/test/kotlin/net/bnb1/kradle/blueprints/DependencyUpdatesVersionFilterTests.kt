package net.bnb1.kradle.blueprints

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import net.bnb1.kradle.blueprints.jvm.DependencyUpdatesBlueprint

class DependencyUpdatesVersionFilterTests : FunSpec({

    test("Exclude alphas, betas, RCs and milestones") {

        val filter = DependencyUpdatesBlueprint.VersionFilter

        filter.reject("1.0.0-alpha").shouldBeTrue()
        filter.reject("1.0.0.alpha").shouldBeTrue()
        filter.reject("1.0.0-alpha1").shouldBeTrue()
        filter.reject("1.0.0.alpha1").shouldBeTrue()
        filter.reject("1.0.0-alpha-1").shouldBeTrue()
        filter.reject("1.0.0.alpha-1").shouldBeTrue()
        filter.reject("1.0.0-alpha.1").shouldBeTrue()
        filter.reject("1.0.0.alpha.1").shouldBeTrue()
        filter.reject("1.0.0-beta").shouldBeTrue()
        filter.reject("1.0.0.beta").shouldBeTrue()
        filter.reject("1.0.0-beta1").shouldBeTrue()
        filter.reject("1.0.0.beta1").shouldBeTrue()
        filter.reject("1.0.0-beta-1").shouldBeTrue()
        filter.reject("1.0.0.beta-1").shouldBeTrue()
        filter.reject("1.0.0-beta.1").shouldBeTrue()
        filter.reject("1.0.0.beta.1").shouldBeTrue()
        filter.reject("1.0.0-RC").shouldBeTrue()
        filter.reject("1.0.0.RC").shouldBeTrue()
        filter.reject("1.0.0-RC1").shouldBeTrue()
        filter.reject("1.0.0.RC1").shouldBeTrue()
        filter.reject("1.0.0-RC-1").shouldBeTrue()
        filter.reject("1.0.0.RC-1").shouldBeTrue()
        filter.reject("1.0.0-RC.1").shouldBeTrue()
        filter.reject("1.0.0.RC.1").shouldBeTrue()
        filter.reject("1.0.0-rc").shouldBeTrue()
        filter.reject("1.0.0.rc").shouldBeTrue()
        filter.reject("1.0.0-rc1").shouldBeTrue()
        filter.reject("1.0.0.rc1").shouldBeTrue()
        filter.reject("1.0.0-rc-1").shouldBeTrue()
        filter.reject("1.0.0.rc-1").shouldBeTrue()
        filter.reject("1.0.0-rc.1").shouldBeTrue()
        filter.reject("1.0.0.rc.1").shouldBeTrue()
        filter.reject("1.0.0-M1").shouldBeTrue()
        filter.reject("1.0.0.M1").shouldBeTrue()
        filter.reject("1.0.0-M-1").shouldBeTrue()
        filter.reject("1.0.0.M-1").shouldBeTrue()
        filter.reject("1.0.0-M.1").shouldBeTrue()
        filter.reject("1.0.0.M.1").shouldBeTrue()
    }
})
