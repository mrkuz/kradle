package net.bnb1.kradle.tasks

open class BootstrapLibTask : AbstractBoostrapTask() {

    override fun stageTwo() {
        if (project.group.toString().isNotEmpty()) {
            val packagePath = project.group.toString().replace(".", "/")
            listOf("main", "test").forEach {
                project.projectDir.resolve("src/$it/kotlin/$packagePath").mkdirs()
            }
        }
    }
}
