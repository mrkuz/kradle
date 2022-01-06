package net.bnb1.kradle.tasks

import org.gradle.api.tasks.TaskAction

open class BootstrapJavaLibTask : BootstrapBaseTask() {

    @TaskAction
    fun run() {
        initializeGit()
        createDirectories("java")
        createFiles()
        copyTextResource("checkstyle.xml")
    }
}
