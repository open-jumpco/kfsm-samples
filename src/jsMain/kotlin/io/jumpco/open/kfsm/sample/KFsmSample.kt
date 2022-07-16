package io.jumpco.open.kfsm.sample

import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main(args: Array<String>) {
    val timeout = if (args.isNotEmpty()) args[0].toDouble() else 5000.0
    val version = kotlin.js.js("process.version")
    println("Starting...$version")
    val lock = Lock()
    val fsm = LockFSM(lock)
    var transisions = 0
    val totalTime = kotlin.time.measureTime {
        val startTime = kotlin.js.Date().getTime()

        while (kotlin.js.Date().getTime() - startTime < timeout) {
            for (i in 0..100) {
                fsm.unlock()
                fsm.lock()
            }
            transisions += 200
        }
    }.inWholeMilliseconds

    val rate = transisions / totalTime
    println("Total transisions $transisions in ${totalTime}ms $rate transisions/ms")
}
