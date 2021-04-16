@file:Suppress("UnstableApiUsage")

pluginManagement {
    val kotlinVersion: String by settings
    val jupyterApiVersion: String by settings
    val libsPublisherVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("jupyter.api") version jupyterApiVersion
        id("ru.ileasile.kotlin.publisher") version libsPublisherVersion
    }
}
