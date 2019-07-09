/*
 * Copyright (c) 2019. Open JumpCO
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.jumpco.open.kfsm.sample
import io.jumpco.open.kfsm.StateMachine

class Lock(initial: Int = 1) {
    var locked: Int = initial
        private set

    fun lock() {
        require(locked == 0)
        locked += 1
    }

    fun doubleLock() {
        require(locked == 1)
        locked += 1
    }

    fun unlock() {
        require(locked == 1)
        locked -= 1
    }

    fun doubleUnlock() {
        require(locked == 2)
        locked -= 1
    }
    fun alarm() {
        // Do nothing
    }

    override fun toString(): String {
        return "Lock(locked=$locked)"
    }

}

enum class LockStates {
    LOCKED,
    DOUBLE_LOCKED,
    UNLOCKED
}

enum class LockEvents {
    LOCK,
    UNLOCK
}

class LockFSM(context: Lock) {
    companion object {
        private fun define() = StateMachine<LockStates, LockEvents, Lock>().stateMachine {
            initial { context ->
                when (context.locked) {
                    0 -> LockStates.UNLOCKED
                    1 -> LockStates.LOCKED
                    2 -> LockStates.DOUBLE_LOCKED
                    else -> error("Invalid state locked=${context.locked}")
                }
            }
            state(LockStates.LOCKED) {
                on(LockEvents.LOCK to LockStates.DOUBLE_LOCKED) { context ->
                    context.doubleLock()
                }
                on(LockEvents.UNLOCK to LockStates.UNLOCKED) { context ->
                    context.unlock()
                }
            }
            state(LockStates.DOUBLE_LOCKED) {
                on(LockEvents.UNLOCK to LockStates.LOCKED) { context ->
                    context.doubleUnlock()
                }
                on(LockEvents.LOCK) { lock -> lock.alarm() }
            }
            state(LockStates.UNLOCKED) {
                on(LockEvents.LOCK to LockStates.LOCKED) { context ->
                    context.lock()
                }
            }
        }.build()

        private val definition by lazy { define() }
    }

    private val fsm = definition.create(context)

    fun unlock() = fsm.sendEvent(LockEvents.UNLOCK)
    fun lock() = fsm.sendEvent(LockEvents.LOCK)
}
