package net.bnb1.kradle

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.implementation(notation: Any) = add("implementation", notation)
fun DependencyHandlerScope.testImplementation(notation: Any) = add("testImplementation", notation)
fun DependencyHandlerScope.testRuntimeOnly(notation: Any) = add("testRuntimeOnly", notation)
