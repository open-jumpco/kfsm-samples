package io.jumpco.open.kfsm.sample

import io.jumpco.open.kfsm.viz.plantUml
import io.jumpco.open.kfsm.viz.visualize
import org.junit.Before
import org.junit.Test
import java.io.File

class SimpleVisualizeFSM {
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
    fun simpleVisualization() {
        val visualization = visualize(LockFSM.definition)
        File("generated", "lock-simple.plantuml").writeText(plantUml(visualization))
    }
}
