package net.bnb1.kradle.support.tasks

import org.gradle.api.tasks.TaskAction

open class BootstrapKotlinLibTask : BootstrapBaseTask() {

    @TaskAction
    fun run() {
        val git = initializeGit()
        createDirectories("kotlin")
        createFiles()
        copyTextResource("detekt-config.yml")
        git.add().addFilepattern(".").call()
    }
}
