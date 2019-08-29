package io.jumpco.open.kfsm.sample

fun main(args: Array<String>) {
    val timeout = if (args.isNotEmpty()) args[0].toLong() else 5000
    val version = "${KotlinVersion.CURRENT.major}.${KotlinVersion.CURRENT.minor}.${KotlinVersion.CURRENT.patch}"
    val os = kotlin.native.Platform.osFamily
    val arch = kotlin.native.Platform.cpuArchitecture
    println("Starting...Kotlin/Native:$version in $os on $arch")
    val lock = Lock()
    val fsm = LockFSM(lock)
    val startTime = kotlin.system.getTimeMillis()
    var iterations = 0
    while (kotlin.system.getTimeMillis() - startTime < timeout) {
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
