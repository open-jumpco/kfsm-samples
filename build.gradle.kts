import org.gradle.internal.os.OperatingSystem
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("multiplatform") version "1.3.61"
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

version = "LOCAL-SNAPSHOT"

val kfsmVersion: String by project

kotlin {
    project.logger.lifecycle("target:${OperatingSystem.current()}")
    jvm()
    js {  nodejs {} }
    /* on ice for now.
    wasm32("wasm") {
        binaries {
            executable {
                entryPoint = "io.jumpco.open.kfsm.sample.main"
            }
        }
    }
     */

    // Create target for the host platform.
    val hostTarget = when {
        OperatingSystem.current().isMacOsX -> macosX64("native")
        OperatingSystem.current().isLinux -> linuxX64("native")
        OperatingSystem.current().isWindows -> mingwX64("native")
        else -> throw GradleException("Host OS '${OperatingSystem.current().name}' is not supported in Kotlin/Native $project.")
    }
    val depSuffix = when {
        OperatingSystem.current().isMacOsX -> "macosX64"
        OperatingSystem.current().isLinux -> "linuxX64"
        OperatingSystem.current().isWindows -> "mingwX64"
        else -> throw GradleException("Host OS '${OperatingSystem.current().name}' is not supported in Kotlin/Native $project.")
    }
    project.logger.lifecycle("target:suffix:$depSuffix")

    hostTarget.apply {
        binaries {
            executable {
                entryPoint = "io.jumpco.open.kfsm.sample.main"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("io.jumpco.open:kfsm-common:$kfsmVersion")
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("io.jumpco.open:kfsm-$depSuffix:$kfsmVersion")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.jumpco.open:kfsm-jvm:$kfsmVersion")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("io.jumpco.open:kfsm-js:$kfsmVersion")
            }
        }
        /*
        val wasmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("io.jumpco.open:kfsm-wasm32:$kfsmVersion")
            }
        }
         */
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
val jvmJar = tasks["jvmJar"]

tasks {

    val fatJar = register("fatJar", Jar::class) {
        archiveBaseName.set("${project.name}-fat")
        manifest {
            attributes["Implementation-Title"] = "kfsm-fat-jar"
            attributes["Implementation-Version"] = archiveVersion
            attributes["Main-Class"] = "io.jumpco.open.kfsm.sample.KFsmSampleKt"
        }
        from(configurations["jvmRuntimeClasspath"].map {
            if (it.isDirectory)
                it
            else
                zipTree(it)
        })
        with(jvmJar as CopySpec)
    }

    register("nativeImage", Exec::class) {
        logging.captureStandardOutput(LogLevel.INFO)
        logging.captureStandardError(LogLevel.ERROR)
        val inputFile = "${project.projectDir}/build/libs/kfsm-samples-fat-$version.jar"
        val outputFile = "${project.projectDir}/build/native/kfsm-samples"
        val cmdLine = listOf(
            "-ea", // -da
            "-H:+ReportExceptionStackTraces",
            "--no-fallback",
            "--static",
            "-O1",
            "--verbose",
            "-jar",
            inputFile,
            outputFile
        )
        val nativeImage = "native-image"
        val output = nativeImage + " " + cmdLine.joinToString(" ")
        doFirst {
            logger.lifecycle("cmd: $output")
            mkdir("./build/native")
        }
        executable = nativeImage
        setArgs(cmdLine)
        inputs.file(inputFile)
        outputs.file(outputFile)
    }
}

afterEvaluate {
    val fatJar = tasks["fatJar"]
    tasks["assemble"].dependsOn(fatJar)
    tasks["nativeImage"].dependsOn(fatJar)
    fatJar.dependsOn(tasks["jvmMainClasses"])
}
