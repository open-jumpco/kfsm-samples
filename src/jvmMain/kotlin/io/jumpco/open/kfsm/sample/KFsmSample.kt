package io.jumpco.open.kfsm.sample

class KFsmSample {
    companion object {
        @JvmStatic
        fun main(args:Array<String>) {
            println("Starting...")
            val lock = Lock()
            val fsm = LockFSM(lock)
            val startTime = System.currentTimeMillis()
            var iterations = 0
            while (System.currentTimeMillis() - startTime < 10000) {
                for (i in 0..100) {
                    fsm.unlock()
                    fsm.lock()
                    iterations += 1
                }
            }
            val totalTime = System.currentTimeMillis() - startTime
            val rate = iterations / totalTime
            println("Total iteration $iterations in ${totalTime}ms $rate iterations/ms")
        }
    }
}
