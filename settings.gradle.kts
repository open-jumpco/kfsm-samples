pluginManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://raw.githubusercontent.com/graalvm/native-build-tools/snapshots")
        }
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/temporary")
        }
        gradlePluginPortal()
        mavenCentral()
    }
}
rootProject.name = "kfsm-samples"
