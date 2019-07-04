import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.40"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.jumpco.open:kfsm-jvm:0.1.0-SNAPSHOT")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Implementation-Title"] = "kfsm-fat-jar"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "io.jumpco.kfsm.sample.KFsmSample"
    }
    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "assemble" {
        dependsOn(fatJar)
    }
}
