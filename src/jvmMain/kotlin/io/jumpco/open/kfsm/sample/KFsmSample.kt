package io.jumpco.open.kfsm.sample

fun main(args: Array<String>) {
    val timeout = if (args.isNotEmpty()) args[0].toLong() else 5000
    val version = System.getProperty("java.version")
    println("Starting...${version}")
    val lock = Lock()
    val fsm = LockFSM(lock)
    val startTime = System.currentTimeMillis()
    var transisions = 0
    while (System.currentTimeMillis() - startTime < timeout) {
        for (i in 0..100) {
            fsm.unlock()
            fsm.lock()
        }
        transisions += 200
    }
    val totalTime = System.currentTimeMillis() - startTime
    val rate = transisions / totalTime
    println("Total transisions $transisions in ${totalTime}ms $rate transisions/ms")
    println()
}
