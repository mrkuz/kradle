# Adding new features

1. Add new entry in [CHANGELOG.md.in](CHANGELOG.md.in)
2. Update [README.md.in](README.md.in)
    1. Add entry to table of contents
    2. Add entry to task list
    3. Add feature description in features section
    4. Update presets
    5. Update configuration DSL reference
3. Create necessary boilerplate
    1. Register new feature
       in [AllFeatures.kt](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/AllFeatures.kt)
    2. Add new dependencies to [buildSrc/Catalog.kt](buildSrc/src/main/kotlin/Catalog.kt)
    3. Run `./gradlew copyCatalog`
    4. Add properties class in [net.bnb1.kradle.blueprint](kradle-plugin/src/main/kotlin/net/bnb1/kradle/blueprints)
    5. Register new properties
       in [AllProperties.kt](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/AllProperties.kt)
    6. Define DSL in [net.bnb1.kradle.config.dsl](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/dsl)
    7. Create empty blueprint in [net.bnb1.kradle.blueprint](kradle-plugin/src/main/kotlin/net/bnb1/kradle/blueprints)
    8. Register new blueprint
       in [AllBlueprints.kt](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/AllBlueprints.kt)
    9. Configure feature in [KradleContext.kt](kradle-plugin/src/main/kotlin/net/bnb1/kradle/config/KradleContext.kt)
    10. Run `./gradlew build` and make sure everything still works

# FAQ

## Naming

### When to `use` and when to use `with` prefix for option names?

- Use `use` if the option just adds dependencies
- Use `with` if the option does more than that
