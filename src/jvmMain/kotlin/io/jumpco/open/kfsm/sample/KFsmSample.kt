package io.jumpco.open.kfsm.sample

fun main(args: Array<String>) {
    val timeout = if (args.isNotEmpty()) args[0].toLong() else 5000
    val version = System.getProperty("java.version")
    println("Starting...${version}")
    val lock = Lock()
    val fsm = LockFSM(lock)
    val startTime = System.currentTimeMillis()
    var iterations = 0
    while (System.currentTimeMillis() - startTime < timeout) {
        for (i in 0..100) {
            fsm.unlock()
            fsm.lock()
            iterations += 1
        }
    }
    val totalTime = System.currentTimeMillis() - startTime
    val rate = iterations / totalTime
    println("Total iteration $iterations in ${totalTime}ms $rate iterations/ms")
    println()
}
