package net.bnb1.kradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class HelloWorldTask : DefaultTask() {

    @TaskAction
    fun run() {
        println("Hello World!");
    }
}