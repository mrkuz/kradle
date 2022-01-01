package net.bnb1.kradle.tasks

import org.gradle.api.tasks.TaskAction

open class BootstrapKotlinLibTask : BootstrapBaseTask() {

    @TaskAction
    fun run() {
        initializeGit()
        createDirectories("kotlin")
        createFiles()
        copyTextResource("detekt-config.yml")
    }
}
