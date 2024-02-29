import io.jumpco.open.kfsm.gradle.VizPluginExtension
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/temporary")
        }
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        }
        mavenCentral()
    }
    dependencies {
        classpath("io.jumpco.open:kfsm-viz-plugin:1.5.2.4")
    }
}

plugins {
    base
    kotlin("multiplatform") version "1.9.22"
    id("io.jumpco.open.kfsm.viz-plugin") version "1.5.2.4"
}

repositories {
    mavenLocal()
    mavenCentral()
}

version = "LOCAL-SNAPSHOT"

val kfsmVersion: String by project

kotlin {
    project.logger.lifecycle("target:${OperatingSystem.current()}")
    jvm()
    js(IR) {
        nodejs()
    }

    wasmJs {
        browser {
            testTask {
                useKarma {
                    useFirefox()
                }
            }
        }
    }


    // Create target for the host platform.
    val nativeTarget = when {
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

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                implementation("io.jumpco.open:kfsm:$kfsmVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation("io.jumpco.open:kfsm-$depSuffix:$kfsmVersion")
            }
        }
        val nativeTest by getting {
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.jumpco.open:kfsm-jvm:$kfsmVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("io.jumpco.open:kfsm-js:$kfsmVersion")
            }
        }
        val jsTest by getting {
            dependencies {
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation("io.jumpco.open:kfsm-wasm:$kfsmVersion")
            }
        }
        val wasmJsTest by getting {
            dependencies {
            }
        }
    }

}
tasks.withType<Test>().configureEach {
    testLogging {
        showStandardStreams = true
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}


afterEvaluate {
    tasks["assemble"].dependsOn(tasks["generateFsmViz"])
}


configure<VizPluginExtension> {
    fsm("LockFSM") {
        outputFolder = file("generated")
        input = file("src/commonMain/kotlin/io/jumpco/open/kfsm/sample/lock.kt")
        isGeneratePlantUml = true // Required default is false
        isGenerateAsciidoc = true // Required default is false
        output = "lock-gen"
    }
}
