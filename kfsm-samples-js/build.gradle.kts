plugins {
    id("org.jetbrains.kotlin.js") version "1.3.40"
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("io.jumpco.open:kfsm-js:0.1.0-SNAPSHOT")
}

kotlin.target.nodejs { }
