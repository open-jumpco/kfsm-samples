package io.jumpco.open.kfsm.sample

fun main(args: Array<String>) {
    val timeout = if(args.isNotEmpty()) args[0].toDouble() else 5000.0
    val version = kotlin.js.js("process.version")
    println("Starting...$version")
    val lock = Lock()
    val fsm = LockFSM(lock)
    val startTime = kotlin.js.Date().getTime()
    var iterations = 0
    while (kotlin.js.Date().getTime() - startTime < timeout) {
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
