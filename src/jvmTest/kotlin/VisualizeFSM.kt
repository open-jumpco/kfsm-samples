package io.jumpco.open.kfsm.sample

import io.jumpco.open.kfsm.viz.Parser.parseStateMachine
import io.jumpco.open.kfsm.viz.Visualization.plantUml
import io.jumpco.open.kfsm.viz.Visualization.asciiDoc
import org.junit.Before
import org.junit.Test
import java.io.File

class VisualizeFSM {
    @Before
    fun setup() {
        val generated = File("generated")
        if (generated.exists() && !generated.isDirectory) {
            error("Expected generated to be a directory")
        } else if (!generated.exists()) {
            generated.mkdirs()
        }
    }
    @Test
    fun visualizeLock() {
        val visualisation = parseStateMachine(
            "LockFSM",
            File("src/commonMain/kotlin/io/jumpco/open/kfsm/sample/lock.kt")
        )
        File("generated", "lock.plantuml").writeText(plantUml(visualisation))
        File("generated", "lock.adoc").writeText(asciiDoc(visualisation))
    }
}
