pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "kradle"
include("kradle-plugin", "kradle-agent")
