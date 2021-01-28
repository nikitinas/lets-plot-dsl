pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://kotlin.bintray.com/kotlin-datascience/")
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlinx.jupyter.api.plugin" -> useModule("org.jetbrains.kotlinx.jupyter:kotlin-jupyter-api-gradle-plugin:0.8.3.157.dev1")
            }
        }
    }
}