package io.jumpco.kfsm.sample

import io.jumpco.open.kfsm.sample.Lock
import io.jumpco.open.kfsm.sample.LockFSM


fun main(args: Array<String>) {
    println("Starting...")
    val lock = Lock()
    val fsm = LockFSM(lock)
    val startTime = kotlin.system.getTimeMillis()
    var iterations = 0
    while (kotlin.system.getTimeMillis() - startTime < 10000) {
        for (i in 0..100) {
            fsm.unlock()
            fsm.lock()
            iterations += 1
        }
    }
    val totalTime = kotlin.system.getTimeMillis() - startTime
    val rate = iterations / totalTime
    println("Total iteration $iterations in ${totalTime}ms $rate iterations/ms")
}
