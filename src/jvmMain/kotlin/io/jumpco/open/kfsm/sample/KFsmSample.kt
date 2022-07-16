package io.jumpco.open.kfsm.sample

import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    val timeout = if (args.isNotEmpty()) args[0].toLong() else 5000
    val version = System.getProperty("java.version")
    println("Starting...${version}")
    val lock = Lock()
    val fsm = LockFSM(lock)
    var transisions = 0
    val totalTime = kotlin.time.measureTime {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < timeout) {
            for (i in 0..100) {
                fsm.unlock()
                fsm.lock()
            }
            transisions += 200
        }
    }.inWholeMilliseconds
    val rate = transisions / totalTime
    println("Total transisions $transisions in ${totalTime}ms $rate transisions/ms")
    println()
}
