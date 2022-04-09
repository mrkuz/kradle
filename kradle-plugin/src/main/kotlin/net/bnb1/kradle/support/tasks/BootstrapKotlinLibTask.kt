package net.bnb1.kradle.support.tasks

import org.gradle.api.tasks.TaskAction

open class BootstrapKotlinLibTask : BootstrapBaseTask() {

    @TaskAction
    fun run() {
        initializeGit()
        createDirectories("kotlin")
        createFiles()
    }
}
