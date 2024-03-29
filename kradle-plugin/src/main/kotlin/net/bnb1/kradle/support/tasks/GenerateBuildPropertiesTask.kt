package net.bnb1.kradle.support.tasks

import net.bnb1.kradle.buildDirAsFile
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

open class GenerateBuildPropertiesTask : DefaultTask() {

    init {
        // Ensure that this task is always executed
        outputs.upToDateWhen { false }
    }

    @get:Internal
    val gitCommit: Property<String> = project.objects.property(String::class.java)

    @get:Internal
    val profile: Property<String> = project.objects.property(String::class.java)

    @SuppressWarnings("MagicNumber")
    @TaskAction
    fun run() {
        val output = project.buildDirAsFile.resolve("resources/main/build.properties")
        output.parentFile.mkdirs()
        output.printWriter().use {
            it.println("project.name=${project.name}")
            it.println("project.group=${project.group}")
            it.println("project.version=${project.properties["version"]}")
            if (profile.isPresent) {
                it.println("build.profile=${profile.get()}")
            }
            it.println("build.timestamp=${System.currentTimeMillis() / 1000}")
            if (gitCommit.isPresent) {
                it.println("git.commit-id=${gitCommit.get()}")
            }
        }
    }
}
