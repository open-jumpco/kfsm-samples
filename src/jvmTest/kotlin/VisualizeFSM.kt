package io.jumpco.open.kfsm.sample

import io.jumpco.open.kfsm.viz.Parser.parseStateMachine
import io.jumpco.open.kfsm.viz.Visualization.asciiDoc
import io.jumpco.open.kfsm.viz.Visualization.plantUml
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
        val visualisation = io.jumpco.open.kfsm.viz.Parser.parseStateMachine(
            "LockFSM",
            File("src/commonMain/kotlin/io/jumpco/open/kfsm/sample/lock.kt")
        )
        File("generated", "lock.plantuml").writeText(io.jumpco.open.kfsm.viz.Visualization.plantUml(visualisation))
        File("generated", "lock.adoc").writeText(io.jumpco.open.kfsm.viz.Visualization.asciiDoc(visualisation))
    }
}
