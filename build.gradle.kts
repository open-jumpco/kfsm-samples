import org.gradle.jvm.tasks.Jar
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("multiplatform") version "1.3.41"
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

version = "LOCAL-SNAPSHOT"


kotlin {
    project.logger.lifecycle("target:${OperatingSystem.current()}")
    jvm()
    js { nodejs {} }
    // Create target for the host platform.
    val hostTarget = when {
        OperatingSystem.current().isMacOsX() -> macosX64("native")
        OperatingSystem.current().isLinux() -> linuxX64("native")
        OperatingSystem.current().isWindows() -> mingwX64("native")
        else -> throw GradleException("Host OS '${OperatingSystem.current().name}' is not supported in Kotlin/Native $project.")
    }
    val depSuffix = when {
        OperatingSystem.current().isMacOsX() -> "macosX64"
        OperatingSystem.current().isLinux() -> "linuxX64"
        OperatingSystem.current().isWindows() -> "mingwX64"
        else -> throw GradleException("Host OS '${OperatingSystem.current().name}' is not supported in Kotlin/Native $project.")
    }
    project.logger.lifecycle("target:suffix:$depSuffix")
    hostTarget.apply {
        binaries {
            executable {
                entryPoint = "io.jumpco.kfsm.sample.main"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("io.jumpco.open:kfsm-common:0.1.0-SNAPSHOT")
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("io.jumpco.open:kfsm-$depSuffix:0.1.0-SNAPSHOT")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.jumpco.open:kfsm-jvm:0.1.0-SNAPSHOT")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("io.jumpco.open:kfsm-js:0.1.0-SNAPSHOT")
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
val jvmJar = tasks["jvmJar"]

tasks {

    val fatJar = register("fatJar", Jar::class) {
        baseName = "${project.name}-fat"
        manifest {
            attributes["Implementation-Title"] = "kfsm-fat-jar"
            attributes["Implementation-Version"] = version
            attributes["Main-Class"] = "io.jumpco.kfsm.sample.KFsmSample"
        }
        from(configurations["jvmRuntimeClasspath"].map({ if (it.isDirectory) it else zipTree(it) }))
        with(jvmJar as CopySpec)
    }
}

afterEvaluate {
    val fatJar = tasks["fatJar"]
    tasks["assemble"].dependsOn(fatJar)
    fatJar.dependsOn(tasks["jvmMainClasses"])
}
