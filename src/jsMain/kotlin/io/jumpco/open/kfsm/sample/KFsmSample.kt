package io.jumpco.open.kfsm.sample

fun main() {
    val version = kotlin.js.js("process.version")
    println("Starting...$version")
    val lock = Lock()
    val fsm = LockFSM(lock)
    val startTime = kotlin.js.Date().getTime()
    var iterations = 0
    while (kotlin.js.Date().getTime() - startTime < 10000) {
        for (i in 0..100) {
            fsm.unlock()
            fsm.lock()
            iterations += 1
        }
    }
    val totalTime = kotlin.js.Date().getTime() - startTime
    val rate = iterations / totalTime
    println("Total iteration $iterations in ${totalTime}ms $rate iterations/ms")
}
