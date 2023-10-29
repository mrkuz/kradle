package net.bnb1.kradle

import io.mockk.MockK
import io.mockk.MockKGateway
import io.mockk.impl.JvmMockKGateway
import kotlin.reflect.KClass

object MockkUtils {

    fun findInvocations(klass: KClass<*>, methodName: String): List<Invocation> {
        MockK.useImpl {
            val gateway = MockKGateway.implementation() as JvmMockKGateway
            return gateway.stubRepo.allStubs
                .filter { it.type == klass }
                .flatMap { it.allRecordedCalls() }
                .filter { it.method.name == methodName }
                .map { Invocation(klass, methodName, it.args) }
                .toList()
        }
    }

    fun clearRecordings() {
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

    data class Invocation(val klass: KClass<*>, val methodName: String, val args: List<Any?>)
}
