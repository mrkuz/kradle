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
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Properties
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.support.Registry
import org.apache.tools.ant.TaskContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginManager
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
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

class GenerateTests {

    @Test
    @Disabled("This is not a test. It's a utility to create test boilerplate")
    fun run() {
        var project = mockk<Project>(relaxed = true)
        val context = spyk(KradleContext(project))
        val registry = Registry()
        val properties = spyAllProperties(registry)
        every { context.blueprints } returns AllBlueprints(registry, properties, project)
        context.initialize()

        val blueprints = collectBlueprintsMetadata(context)
        blueprints.forEach { generateTest(it, properties) }
    }

    private fun spyAllProperties(registry: Registry): AllProperties {
        val spy = spyk(AllProperties(registry))
        AllProperties::class.getMemberProperties(Properties::class).forEach {
            every { spy getProperty it.name } answers { spyk(callOriginal() as Any) }
        }
        return spy
    }

    private fun collectBlueprintsMetadata(context: KradleContext): List<BlueprintMetadata> {
        val blueprints = mutableListOf<BlueprintMetadata>()
        AllBlueprints::class.getMemberProperties(Blueprint::class).forEach {
            val blueprint = it.get(context.blueprints)
            clearRecordings()
            runBlueprintMethods(blueprint)
            val properties = getAccessedProperties()
            blueprints.add(BlueprintMetadata(blueprint, properties))
            clearRecordings()
        }
        return blueprints
    }

    private fun clearRecordings() {
        MockK.useImpl {
            val clearOptions = MockKGateway.ClearOptions(
                answers = false,
                recordedCalls = true,
                childMocks = false,
                verificationMarks = false,
                exclusionRules = false
            )
            val gateway = MockKGateway.implementation() as JvmMockKGateway
            gateway.stubRepo.allStubs.forEach {
                it.clear(clearOptions)
            }
        }
    }

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

    @Suppress("UncheckedCast")
    private fun getAccessedProperties(): List<PropertyAccess> {
        val properties = mutableListOf<PropertyAccess>()
        MockK.useImpl {
            val gateway = MockKGateway.implementation() as JvmMockKGateway
            gateway.stubRepo.allStubs.forEach { stub ->
                if (stub.type.isSubclassOf(Properties::class)) {
                    stub.allRecordedCalls().forEach { invocation ->
                        val fieldName = invocation.fieldValueProvider.invoke()!!.name
                        properties.add(PropertyAccess(stub.type as KClass<Properties>, fieldName))
                    }
                }
            }
        }
        return properties
    }

    private fun getInvocations(klass: KClass<*>, methodName: String): List<Invocation> {
        val invocations = mutableListOf<Invocation>()
        MockK.useImpl {
            val gateway = MockKGateway.implementation() as JvmMockKGateway
            gateway.stubRepo.allStubs.forEach { stub ->
                if (stub.type == klass) {
                    stub.allRecordedCalls().forEach { invocation ->
                        if (invocation.method.name == methodName) {
                            invocations.add(Invocation((invocation.args)))
                        }
                    }
                }
            }
        }
        return invocations
    }

    private fun generateTest(metadata: BlueprintMetadata, allProperties: AllProperties) {
        val dir = Path.of(
            System.getenv("PROJECT_DIR"), "src", SOURCE_SET, "kotlin", "net", "bnb1", "kradle", "blueprints"
        ).toFile()

        dir.mkdirs()

        val className = metadata.blueprint::class.simpleName + "Tests"
        val file = dir.resolve("$className.kt")
        if (file.exists()) {
            return
        }

        file.writeText(
            """
            package net.bnb1.kradle.blueprints

            import io.kotest.core.spec.style.BehaviorSpec

            class $className : BehaviorSpec({
            
            """.trimIndent()
        )

        generateGiven(file, metadata, "Default configuration")

        val wrapper = AllPropertiesWrapper(allProperties)
        metadata.properties.forEach {
            val value = wrapper.getValue(it)
            val propertyName = wrapper.getPropertiesName(it) + "." + it.name
            if (value is Flag) {
                value.toggle()
                generateGiven(file, metadata, "$propertyName = ${value.get()}")
                value.toggle()
            }
            if (value is Value<*> && !value.hasValue && value.hasSuggestion) {
                value.set()
                generateGiven(file, metadata, "$propertyName = ${value.get()}")
                value.reset()
            }
        }

        file.appendText(
            """
            })
            """.trimIndent()
        )
    }

    private fun generateGiven(file: File, metadata: BlueprintMetadata, givenText: String) {
        file.appendText(
            """
            |    
            |    Given("$givenText") {
        """.trimMargin("|")
        )

        generateWhens(file, metadata)

        file.appendText(
            """
            |    }
            |
        """.trimMargin("|")
        )
    }

    private fun generateWhens(file: File, metadata: BlueprintMetadata) {
        clearRecordings()
        runBlueprintMethods(metadata.blueprint)

        var invocations = getInvocations(PluginManager::class, "apply")
        if (invocations.isNotEmpty()) {
            generateWhenThen(
                file,
                "Check for plugin", "Plugin is applied",
                invocations.flatMap { it.args }.joinToString(", ")
            )
        }

        invocations = getInvocations(TaskContainer::class, "create")
        if (invocations.isNotEmpty()) {
            generateWhenThen(
                file,
                "List tasks", "Task is available",
                invocations.flatMap { it.args }.joinToString(", ")
            )
        }

        invocations = (
            getInvocations(DependencyHandler::class, "create") +
                getInvocations(DependencyHandler::class, "add")
            )
        if (invocations.isNotEmpty()) {
            generateWhenThen(
                file,
                "List dependencies", "Dependencies are available",
                invocations.flatMap { it.args }.joinToString(", ")
            )
        }

        clearRecordings()
    }

    private fun generateWhenThen(file: File, whenText: String, thenText: String, comment: String) {
        file.appendText(
            """
            |        
            |        When("$whenText") {
            |            
            |            Then("$thenText") {
            |                // $comment
            |            }
            |        }
            |
        """.trimMargin("|")
        )
    }
}

data class BlueprintMetadata(val blueprint: Blueprint, val properties: List<PropertyAccess>)

data class PropertyAccess(val propertiesClass: KClass<Properties>, val name: String) {

    fun getValue(properties: Properties) = propertiesClass.memberProperties
        .find { it.name == name }!!
        .get(properties)!!
}

data class Invocation(val args: List<Any?>)

class AllPropertiesWrapper(private val allProperties: AllProperties) {

    fun getPropertiesName(access: PropertyAccess) = AllProperties::class.memberProperties
        .find { it.returnType.jvmErasure == access.propertiesClass }!!.name

    fun getValue(access: PropertyAccess): Any {
        val properties = AllProperties::class.memberProperties
            .find { it.returnType.jvmErasure == access.propertiesClass }!!
            .get(allProperties) as Properties
        return access.getValue(properties)
    }
}

@Suppress("UncheckedCast")
fun <T : Any, U : Any> KClass<T>.getMemberProperties(subclassOf: KClass<U>): List<KProperty1<T, U>> {
    return memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filter { it.returnType.jvmErasure.isSubclassOf(subclassOf) }
        .toList() as List<KProperty1<T, U>>
}
