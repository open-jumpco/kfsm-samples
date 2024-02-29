package io.jumpco.open.kfsm.sample

import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource
import kotlin.time.measureTime

class KFSMSampleTest {

    @Test
    fun testSample() {

        val lock = Lock()
        val fsm = LockFSM(lock)
        var transitions = 0
        val timeout = 1500.milliseconds
        val totalTime = measureTime {
            val startTime = TimeSource.Monotonic.markNow()
            do {
                for (i in 1..10000) {
                    fsm.unlock()
                    fsm.lock()
                    transitions += 2
                }
                val endTime = TimeSource.Monotonic.markNow()
                if (startTime.plus(timeout) < endTime) {
                    break
                }
            } while (true)
        }

        val rate = transitions.toDouble() * 1000.0 / totalTime.inWholeMicroseconds.toDouble()
        println("Total transitions $transitions in ${totalTime.inWholeMilliseconds}ms ${rate.toLong() * 1000L / 1000L} transisions/ms")
        println()
    }
}