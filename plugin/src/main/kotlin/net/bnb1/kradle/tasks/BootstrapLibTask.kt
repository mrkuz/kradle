package net.bnb1.kradle.tasks

open class BootstrapLibTask : AbstractBoostrapTask() {

    override fun stageTwo() {
        if (project.group.toString().isEmpty()) {
            project.logger.warn("WARNING: Group is not specified")
        } else {
            val packagePath = project.group.toString().replace(".", "/")
            listOf("main", "test").forEach {
                project.rootDir.resolve("src/$it/kotlin/$packagePath").mkdirs()
            }
        }
    }
}