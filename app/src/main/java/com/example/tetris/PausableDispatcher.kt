package com.example.tetris

import android.os.Handler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import java.util.*
import kotlin.coroutines.CoroutineContext

/*
*
*   https://github.com/Kotlin/kotlinx.coroutines/issues/104#issuecomment-345061447
*
*/

class PausableDispatcher(private val handler: Handler): CoroutineDispatcher() {

    private val queue: Queue<Runnable> = LinkedList()
    private var isPaused = false

    @Synchronized override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (isPaused) queue.add(block) else handler.post(block)
    }

    private fun runQueue() {
        queue.iterator().let {
            while (it.hasNext()) {
                val block = it.next()
                it.remove()
                handler.post(block)
            }
        }
    }

    @Synchronized fun pause() {
        isPaused = true
    }

    @Synchronized fun resume() {
        isPaused = false
        runQueue()
    }
}