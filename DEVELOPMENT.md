# Adding new features

1. Add new entry in [CHANGELOG.md.in](CHANGELOG.md.in)
2. Update [README.md.in](README.md.in)
    1. Add entry to table of contents
    2. Add entry to task list
    3. Add feature description in features section
    4. Update presets
    5. Update configuration DSL reference
3. Update [examples](examples)
4. Create necessary boilerplate
    1. Register new feature
       in [AllFeatures.kt](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/AllFeatures.kt)
    2. Add new dependencies to [buildSrc/Catalog.kt](buildSrc/src/main/kotlin/Catalog.kt)
    3. Run `./gradlew copyCatalog`
    4. Add properties class in [net.bnb1.kradle.blueprint](kradle-plugin/src/main/kotlin/net/bnb1/kradle/blueprints)
    5. Register new properties
       in [AllProperties.kt](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/AllProperties.kt)
    6. Create DSL class in [net.bnb1.kradle.config.dsl](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/dsl)
    7. Choose a parent for the new DSL element in [net.bnb1.kradle.config.dsl](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/dsl)
    8. Create empty blueprint in [net.bnb1.kradle.blueprint](kradle-plugin/src/main/kotlin/net/bnb1/kradle/blueprints)
    9. Register new blueprint
       in [AllBlueprints.kt](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/AllBlueprints.kt)
    10. Configure feature in [KradleContext.kt](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/KradleContext.kt)
    11. Run `./gradlew build` and make sure everything still works
5. Create unit tests in [src/test](kradle-plugin/src/test)
6. Create integration tests for blueprint in [src/integrationTest](kradle-plugin/src/integrationTest)
    1. Create a test for the default configuration
    2. Create a test for each configuration option
7. Implement the new feature

# Release

1. Switch to stable branch `git checkout stable`
2. Merge main branch `git merge main`
3. Update version in [build.gradle.kts](kradle-plugin/build.gradle.kts)
4. Generate updated _README.md_ and _CHANGELOG.md_ `./gradlew renderTemplates`
5. Commit changes

   ```shell
   git add -u
   git commit -m"Release vX.Y.Z"
   ```
6. Create a clean build, which also runs all checks and tests `./gradlew clean build`
7. Tag and push changes

   ```shell
   git tag vX.Y.Z
   git push
   git push origin vX.Y.Z
   ```

8. Publish plugins `./gradlew publishPlugins`
9. Switch to main branch `git checkout main`
10. Merge stable branch, but don't commit `git merge --no-commit stable`
11. Set version in [build.gradle.kts](kradle-plugin/build.gradle.kts) back to `main-SNAPSHOT`
12. Prepare [CHANGELOG.md.in](CHANGELOG.md.in) for next release
13. Generate updated _README.md_ and _CHANGELOG.md_ `./gradlew renderTemplates`
14. Commit and push changes

    ```shell
    git add -u
    git commit
    git push
    ```

15. Use the new deployed plugin [build.gradle.kts](kradle-plugin/build.gradle.kts)
16. Make sure everything works `./gradlew clean build`
17. Commit and push changes

    ```shell
    git add -u
    git commit -m"Use self vX.Y.Z"
    git push
    ```

# FAQ

## Miscellaneous

### How to install the plugin locally?

```shell
./gradlew publishToMavenLocal
```

## Naming

### When to `use` and when to use `with` prefix for option names?

- Use no prefix if the option is a simple on/off switch
- Use `use` if the option just adds dependencies
- Use `with` if the option does more than that
