import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ru.ileasile.kotlin.apache2
import ru.ileasile.kotlin.developer
import ru.ileasile.kotlin.githubRepo

plugins {
    kotlin("jvm")
    kotlin("jupyter.api")
    id("ru.ileasile.kotlin.publisher")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

val dataframeVersion: String by project
val letsPlotLibVersion: String by project
val letsPlotApiVersion: String by project

dependencies {
    val dataFrameDependency = "org.jetbrains.kotlinx:dataframe:$dataframeVersion"
    compileOnly(dataFrameDependency)
    testImplementation(dataFrameDependency)

    implementation("org.jetbrains.lets-plot:lets-plot-common:$letsPlotLibVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:$letsPlotLibVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-api-kernel:$letsPlotApiVersion")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.5")

    testImplementation("junit:junit:4.12")
    testImplementation("io.kotlintest:kotlintest-assertions:3.1.6")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

fun detectVersion(): String {
    val buildNumber = rootProject.findProperty("build.number") as? String
    val defaultVersion = property("version").toString()

    return if (buildNumber != null) {
        if (hasProperty("build.number.detection")) {
            "$defaultVersion-dev-$buildNumber"
        } else {
            buildNumber
        }
    }
    else if(hasProperty("release")) {
        defaultVersion
    } else {
        "$defaultVersion-dev"
    }
}

val detectVersionForTC: Task by tasks.creating {
    doLast {
        println("##teamcity[buildNumber '$version']")
    }
}

group  = "org.jetbrains.kotlinx"
version = detectVersion()

kotlinPublications {
    fun prop(name: String) = project.findProperty(name) as? String?

    sonatypeSettings(
        prop("kds.sonatype.user"),
        prop("kds.sonatype.password"),
        "lets-plot-dsl project, v. ${project.version}"
    )

    signingCredentials(
        prop("kds.sign.key.id"),
        prop("kds.sign.key.private"),
        prop("kds.sign.key.passphrase")
    )

    pom {
        githubRepo("nikitinas", "lets-plot-dsl")
        inceptionYear.set("2021")
        licenses {
            apache2()
        }
        developers {
            developer("nikitinas", "Anatoly Nikitin", "Anatoly.Nikitin@jetbrains.com")
        }
    }

    publication {
        publicationName = "api"
        artifactId = "lets-plot-dsl"
        description = "Kotlin DSL for convenient Lets-Plot usage inside Jupyter Notebooks"
        packageName = artifactId
    }
}
