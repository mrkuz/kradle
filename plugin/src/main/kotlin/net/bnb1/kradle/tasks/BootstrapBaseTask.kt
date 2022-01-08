package net.bnb1.kradle.tasks

import org.eclipse.jgit.api.Git
import org.gradle.api.DefaultTask

open class BootstrapBaseTask : DefaultTask() {

    init {
        dependsOn(":wrapper")
    }

    protected fun initializeGit() {
        copyTextResource("gitignore", ".gitignore")
        if (!project.rootDir.resolve(".git").exists()) {
            Git.init().setDirectory(project.rootDir).call()
        }
    }

    protected fun copyTextResource(name: String) = copyTextResource(name, name)

    fun copyTextResource(resource: String, to: String) {
        val target = project.rootDir.resolve(to)
        if (!target.exists()) {
            target.writeText(javaClass.getResource("/$resource")!!.readText())
        }
    }

    protected fun createDirectories(sourceDirectory: String) {
        listOf(sourceDirectory, "resources", "extra").forEach {
            project.projectDir.resolve("src/main/$it").mkdirs()
        }
        listOf(sourceDirectory, "resources").forEach {
            project.projectDir.resolve("src/test/$it").mkdirs()
        }
        project.projectDir.resolve("src/benchmark/$sourceDirectory").mkdirs()

        if (project.group.toString().isNotEmpty()) {
            val packagePath = project.group.toString().replace(".", "/")
            listOf("main", "test").forEach {
                project.projectDir.resolve("src/$it/$sourceDirectory/$packagePath").mkdirs()
            }
        }
    }

    protected fun createFiles() {
        listOf("README.md", "LICENSE", "project.properties").forEach {
            project.rootDir.resolve(it).createNewFile()
        }
    }
}
