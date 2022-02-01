@file:Suppress("UNCHECKED_CAST")

package net.bnb1.kradle

import io.mockk.MockK
import io.mockk.MockKGateway
import io.mockk.every
import io.mockk.impl.JvmMockKGateway
import io.mockk.mockk
import io.mockk.spyk
import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.config.KradleContext
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.core.FeatureSet
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Properties
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.support.Registry
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginManager
import org.gradle.api.tasks.TaskContainer
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

private const val SOURCE_SET = "generatedTest"
private const val TRIPLE_QUOTES = "\"\"\""

class GenerateBlueprintTests {

    @Test
    @Disabled("Utility to create blueprint test boilerplate")
    fun run() {
        val project = mockk<Project>(relaxed = true)
        every { project.tasks } returns mockk()
        val context = spyk(KradleContext(project))
        val registry = Registry()
        val properties = spyAllProperties(registry)
        every { context.properties } returns properties
        every { context.blueprints } returns AllBlueprints(registry, properties, project)
        context.initialize()

        val blueprints = collectBlueprintsMetadata(context)
        blueprints.forEach { TestGenerator(context, it).generate() }
    }

    private fun spyAllProperties(registry: Registry): AllProperties {
        val spy = spyk(AllProperties(registry))
        AllProperties::class.getMemberProperties(Properties::class).forEach {
            every { spy getProperty it.name } answers { spyk(callOriginal() as Any) }
        }
        return spy
    }

    private fun collectBlueprintsMetadata(context: KradleContext): List<BlueprintMetadata> {
        val metadata = mutableListOf<BlueprintMetadata>()
        AllBlueprints::class.getMemberProperties(Blueprint::class).forEach {
            val blueprint = it.get(context.blueprints)
            MockkUtils.clearRecordings()
            runBlueprintMethods(blueprint)
            val accessedProperties = getAccessedProperties()
            metadata.add(BlueprintMetadata(blueprint, accessedProperties))
            MockkUtils.clearRecordings()
        }
        return metadata
    }

    private fun getAccessedProperties(): List<PropertyAccess> {
        val accessedProperties = mutableListOf<PropertyAccess>()
        MockK.useImpl {
            val gateway = MockKGateway.implementation() as JvmMockKGateway
            gateway.stubRepo.allStubs
                .filter { stub -> stub.type.isSubclassOf(Properties::class) }
                .forEach { stub ->
                    stub.allRecordedCalls().forEach { invocation ->
                        val fieldName = invocation.fieldValueProvider.invoke()!!.name
                        accessedProperties.add(PropertyAccess(stub.type as KClass<Properties>, fieldName))
                    }
                }
        }
        return accessedProperties
    }
}

class TestGenerator(context: KradleContext, private val metadata: BlueprintMetadata) {

    private val wrapper = KradleContextWrapper(context)
    private val featureSet = wrapper.getFeatureSet(metadata)
    private val feature = wrapper.getFeature(metadata)

    private val className = metadata.blueprint::class.simpleName + "Tests"
    private val dir = Path.of(
        System.getenv("PROJECT_DIR"),
        "src",
        SOURCE_SET,
        "kotlin",
        "net",
        "bnb1",
        "kradle",
        "blueprints"
    )
    private val output = dir.toFile().resolve("$className.kt")

    private val processed = mutableSetOf<MockkUtils.Invocation>()

    fun generate() {
        dir.toFile().mkdirs()

        output.writeText(
            """
            package net.bnb1.kradle.blueprints

            import io.kotest.core.spec.style.BehaviorSpec
            import io.kotest.matchers.shouldBe
            import net.bnb1.kradle.TestProject
            import org.gradle.testkit.runner.TaskOutcome

            class $className : BehaviorSpec({
            
                val project = TestProject(this)
                
            """.trimIndent()
        )

        generateGiven("Default configuration", 4) {
            """
            project.setUp {
               $TRIPLE_QUOTES
               ${featureSet.name} {
                   ${feature.name}.enable()
               }
               $TRIPLE_QUOTES.trimIndent()
            }
            
            When("Run ${feature.defaultTaskName}") {
                val result = project.runTask("${feature.defaultTaskName}")
                
                Then("Succeed") {
                    result.task(":${feature.defaultTaskName}")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
            """
        }

        metadata.properties.forEach {
            val value = wrapper.getValue(it)
            val propertiesName = wrapper.getPropertiesName(it)
            val propertyName = propertiesName + "." + it.name
            if (value is Flag) {
                value.toggle()
                generateGiven("$propertyName = ${value.get()}", 4) {
                    guessProjectSetup(it, value.get())
                }
                value.toggle()
            }
            if (value is Value<*> && !value.hasValue && value.hasSuggestion) {
                value.set()
                generateGiven("$propertyName = ${value.get()}", 4) {
                    guessProjectSetup(it, value.get())
                }
                value.reset()
            }
        }

        output.appendText("})")
    }

    private fun guessProjectSetup(access: PropertyAccess, value: Any): String {
        // It would be better to use KradleExtension instead of guessing, but for now this is fine
        val propertiesName = wrapper.getPropertiesName(access)
        val featureExists = wrapper.context.featuresAsList().any { it.name == propertiesName }
        return if (featureExists) {
            """
            project.setUp {
                $TRIPLE_QUOTES
                ${featureSet.name} {
                    $propertiesName {
                        ${access.name}($value)
                    }
                }
                $TRIPLE_QUOTES.trimIndent()
            }
            """
        } else {
            """
            project.setUp {
                $TRIPLE_QUOTES
                ${featureSet.name} {
                    ${feature.name} {
                        $propertiesName {
                            ${access.name}($value)
                        }
                    }
                }
                $TRIPLE_QUOTES.trimIndent()
            }  
            """
        }
    }

    private fun generateGiven(givenText: String, indent: Int, body: () -> String) {
        output.appendText(
            """

            Given("$givenText") {
            """.block(indent)
        )

        output.appendText(body().block(indent + 4))

        generateWhens(indent + 4)

        output.appendText("}".block(indent))
    }

    private fun generateWhens(indent: Int) {
        MockkUtils.clearRecordings()
        runBlueprintMethods(metadata.blueprint)

        var invocations = findInvocations(PluginManager::class, "apply", 1)
        handlePlugins(invocations, indent)

        invocations = findInvocations(TaskContainer::class, "create", 1)
        handleTasks(invocations, indent)

        invocations = mutableListOf()
        invocations = invocations + findInvocations(DependencyHandler::class, "create", 2)
        invocations = invocations + findInvocations(DependencyHandler::class, "add", 2)
        handleDependencies(invocations, indent)

        MockkUtils.clearRecordings()
    }

    private fun handlePlugins(invocations: List<MockkUtils.Invocation>, indent: Int) {
        if (invocations.isEmpty()) return

        output.appendText(
            """
                
            When("Check for plugins") {
            """.block(indent)
        )

        invocations
            .map { it.args[0] }
            .filterIsInstance<Class<*>>()
            .forEach {
                output.appendText(
                    """
                        
                    Then("${it.simpleName} is applied") {
                        project.shouldHavePlugin(${it.simpleName}::class)
                    }
                    """.block(indent + 4)
                )
            }

        output.appendText("}".block(indent))
    }

    private fun handleTasks(invocations: List<MockkUtils.Invocation>, indent: Int) {
        if (invocations.isEmpty()) return

        output.appendText(
            """
                
            When("Check for tasks") {
            """.block(indent)
        )

        val tasks = invocations
            .map { it.args[0] }
            .filterIsInstance<String>()

        tasks.forEach {
            output.appendText(
                """
                        
                Then("Task $it is available") {
                    project.shouldHaveTask("$it")
                }
                """.block(indent + 4)
            )
        }

        tasks.forEach {
            output.appendText(
                """       
                
                When("Run $it") {
                    val result = project.runTask("$it")

                    Then("Succeed") {
                        result.task(":$it")!!.outcome shouldBe TaskOutcome.SUCCESS
                    }
                }
                """.block(indent + 4)
            )
        }

        output.appendText("}".block(indent))
    }

    private fun handleDependencies(invocations: List<MockkUtils.Invocation>, indent: Int) {
        if (invocations.isEmpty()) return

        output.appendText(
            """
            
            When("Check for dependencies") {
            """.block(indent)
        )

        invocations
            .map { it.args }
            .filter { it.size == 2 }
            .filter { it[0] is String && it[1] is String }
            .forEach {
                output.appendText(
                    """
                        
                    Then("${it[1].toString().split(":")[1]} is available") {
                        project.shouldHaveDependency("${it[0]}", "${it[1]}")
                    }
                    """.block(indent + 4)
                )
            }

        output.appendText("}".block(indent))
    }

    private fun findInvocations(klass: KClass<*>, methodName: String, argsLimit: Int): List<MockkUtils.Invocation> {
        return MockkUtils.findInvocations(klass, methodName)
            .filter { processed.add(MockkUtils.Invocation(it.klass, it.methodName, it.args.slice(0 until argsLimit))) }
            .toList()
    }
}

data class BlueprintMetadata(val blueprint: Blueprint, val properties: List<PropertyAccess>)

data class PropertyAccess(val propertiesClass: KClass<Properties>, val name: String) {

    fun getValue(properties: Properties) = propertiesClass.memberProperties
        .find { it.name == name }!!
        .get(properties)!!
}

class KradleContextWrapper(val context: KradleContext) {

    fun getPropertiesName(access: PropertyAccess) = AllProperties::class.memberProperties
        .find { it.returnType.jvmErasure == access.propertiesClass }!!.name

    fun getValue(access: PropertyAccess): Any {
        val properties = AllProperties::class.memberProperties
            .find { it.returnType.jvmErasure == access.propertiesClass }!!
            .get(context.properties) as Properties
        return access.getValue(properties)
    }

    fun getFeature(metadata: BlueprintMetadata): Feature {
        return context.featuresAsList().find { it.hasBlueprint(metadata.blueprint::class) }!!
    }

    fun getFeatureSet(metadata: BlueprintMetadata): FeatureSet {
        val feature = context.featuresAsList().find { it.hasBlueprint(metadata.blueprint::class) }!!
        return context.featuresSetsAsList().find { it.hasFeature(feature) }!!
    }
}

@SuppressWarnings("SwallowedException")
private fun runBlueprintMethods(blueprint: Blueprint) {
    blueprint::class.memberFunctions
        .filter { it.isOpen }
        .forEach {
            try {
                it.isAccessible = true
                it.call(blueprint)
            } catch (e: Exception) {
                // Ignore
            }
        }
}

fun <T : Any, U : Any> KClass<T>.getMemberProperties(subclassOf: KClass<U>): List<KProperty1<T, U>> {
    return memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filter { it.returnType.jvmErasure.isSubclassOf(subclassOf) }
        .toList() as List<KProperty1<T, U>>
}

fun String.block(indent: Int): String {
    val lines = this.trimIndent().lines()
    return lines.joinToString("\n") {
        if (it.isBlank()) {
            ""
        } else {
            " ".repeat(indent) + it
        }
    } + "\n"
}
