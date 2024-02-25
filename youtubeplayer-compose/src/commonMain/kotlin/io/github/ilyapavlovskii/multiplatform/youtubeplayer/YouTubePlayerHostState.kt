package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

interface YouTubePlayerState {
    var command: YouTubeExecCommand?

    fun complete()
}

@Stable
class YouTubePlayerHostState {
    /**
     * Only one [YouTubePlayer] can be shown at a time.
     * Since a suspending Mutex is a fair queue, this manages our message
     * queue and we don't have to maintain one.
     */
    private val mutex = Mutex()

    var currentState by mutableStateOf<YouTubePlayerState?>(null)
        private set

    suspend fun executeCommand(
        command: YouTubeExecCommand
    ): Unit = mutex.withLock {
        try {
            suspendCancellableCoroutine {
                currentState = YouTubePlayerStateImpl(command, it)
            }
        } finally {
            currentState = null
        }
    }

    @Stable
    private class YouTubePlayerStateImpl(
        override var command: YouTubeExecCommand?,
        private val continuation: CancellableContinuation<Unit>
    ) : YouTubePlayerState {
        override fun complete() {
            if (continuation.isActive) continuation.resume(Unit)
        }
    }
}
