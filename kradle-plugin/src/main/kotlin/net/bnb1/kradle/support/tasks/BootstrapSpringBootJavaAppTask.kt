package net.bnb1.kradle.support.tasks

import net.bnb1.kradle.empty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path

open class BootstrapSpringBootJavaAppTask : BootstrapBaseTask() {

    @get:Input
    val mainClass = project.objects.empty<String>()

    @TaskAction
    fun run() {
        createDirectories("java")
        createFiles()

        val packageName = mainClass.get().replace(Regex(".[^.]+$"), "")
        val path = Path.of(mainClass.get().replace(".", "/"))
        val mainClassName = path.last().toString()
        val packagePath = path.parent.toString()

        listOf("main", "test").forEach {
            project.projectDir.resolve("src/$it/java/$packagePath").mkdirs()
        }

        val appSource = project.projectDir.resolve("src/main/java/$packagePath/$mainClassName.java")
        if (!appSource.exists()) {
            appSource.writeText(
                """
                package $packageName;
                
                import org.springframework.boot.CommandLineRunner;
                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;
                
                @SpringBootApplication
                public class $mainClassName implements CommandLineRunner {
                
                    public static void main(String[] args) {
                        SpringApplication.run(${mainClassName}.class, args);
                    }
                
                    @Override
                    public void run(String... args) throws Exception {
                        System.out.println("Hello World!");
                    }
                }
                
                """.trimIndent()
            )
        }

        val applicationProperties = project.projectDir.resolve("src/main/resources/application.properties")
        if (!applicationProperties.exists()) {
            applicationProperties.createNewFile()
        }
    }
}
