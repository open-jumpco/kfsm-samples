import org.gradle.internal.os.OperatingSystem
plugins {
    kotlin("multiplatform") version "1.3.40"
}

kotlin {

    // Create target for the host platform.
    val hostTarget = when {
        OperatingSystem.current().isMacOsX() -> macosX64("native")
        OperatingSystem.current().isLinux() -> linuxX64("native")
        OperatingSystem.current().isWindows() -> mingwX64("native")
        else -> throw GradleException("Host OS '${OperatingSystem.current().name}' is not supported in Kotlin/Native $project.")
    }
    println("Host Target $hostTarget")
    val depSuffix = when {
        OperatingSystem.current().isMacOsX() -> "macosX64"
        OperatingSystem.current().isLinux() -> "linuxX64"
        OperatingSystem.current().isWindows() -> "mingwX64"
        else -> throw GradleException("Host OS '${OperatingSystem.current().name}' is not supported in Kotlin/Native $project.")
    }
    println("Suffix $depSuffix")
    hostTarget.apply {
        binaries {
            executable {
                entryPoint = "io.jumpco.kfsm.sample.main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation("io.jumpco.open:kfsm-$depSuffix:0.1.0-SNAPSHOT")
            }
        }
    }
}

// Use the following Gradle tasks to run your application:
// :runReleaseExecutableMingw - without debug symbols
// :runDebugExecutableMingw - with debug symbols
