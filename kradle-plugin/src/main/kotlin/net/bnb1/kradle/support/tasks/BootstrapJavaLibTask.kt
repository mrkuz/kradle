package net.bnb1.kradle.support.tasks

import org.gradle.api.tasks.TaskAction

open class BootstrapJavaLibTask : BootstrapBaseTask() {

    @TaskAction
    fun run() {
        createDirectories("java")
        createFiles()
    }
}
