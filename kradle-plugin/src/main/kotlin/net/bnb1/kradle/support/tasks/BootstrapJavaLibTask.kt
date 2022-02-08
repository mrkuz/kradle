package net.bnb1.kradle.support.tasks

import org.gradle.api.tasks.TaskAction

open class BootstrapJavaLibTask : BootstrapBaseTask() {

    @TaskAction
    fun run() {
        val git = initializeGit()
        createDirectories("java")
        createFiles()
        copyTextResource("checkstyle.xml")
        copyTextResource("lombok.config")
        git.add().addFilepattern(".").call()
    }
}
