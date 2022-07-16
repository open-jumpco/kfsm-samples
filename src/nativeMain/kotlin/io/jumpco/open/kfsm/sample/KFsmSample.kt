package io.jumpco.open.kfsm.sample

import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalStdlibApi
fun main(args: Array<String>) {
    val timeout = if (args.isNotEmpty()) args[0].toLong() else 5000
    val version = "${KotlinVersion.CURRENT.major}.${KotlinVersion.CURRENT.minor}.${KotlinVersion.CURRENT.patch}"
    val os = kotlin.native.Platform.osFamily
    val arch = kotlin.native.Platform.cpuArchitecture
    val newMM = kotlin.native.isExperimentalMM()
    println("Starting...Kotlin/Native:$version in $os on $arch newMM=$newMM")
    val lock = Lock()
    val fsm = LockFSM(lock)
    var transisions = 0
    val totalTime = kotlin.time.measureTime {
        val startTime = kotlin.system.getTimeMillis()
        while (kotlin.system.getTimeMillis() - startTime < timeout) {
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
