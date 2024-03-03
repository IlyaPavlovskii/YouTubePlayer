package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeEvent
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.time.Duration

/**
 * YouTube player state defines actual youtube player state on the screen
 * */
sealed class YouTubePlayerState {
    /**
     * Idle state means that player is not initialized yet
     * */
    data object Idle : YouTubePlayerState()

    /**
     * Means that player is ready to play
     * */
    data object Ready : YouTubePlayerState()

    /**
     * Means that player is playing video.
     *
     * @param videoId - id of the video that is playing
     * @param duration - duration of the video
     * @param currentTime - current time of the video
     * @param quality - quality of the video, see [YouTubeEvent.PlaybackQualityChange.Quality]
     * @param isPlaying - is video playing
     * */
    data class Playing(
        val videoId: YouTubeVideoId,
        val duration: Duration = Duration.ZERO,
        val currentTime: Duration = Duration.ZERO,
        val quality: YouTubeEvent.PlaybackQualityChange.Quality =
            YouTubeEvent.PlaybackQualityChange.Quality.SMALL,
        val isPlaying: Boolean = false,
    ) : YouTubePlayerState()

    /**
     * Defines error state
     * */
    data class Error(
        val message: String,
    ) : YouTubePlayerState()
}

@Stable
class YouTubePlayerHostState {
    /**
     * Only one [YouTubePlayer] can be shown at a time.
     * Since a suspending Mutex is a fair queue, this manages our message
     * queue and we don't have to maintain one.
     */
    private val mutex = Mutex()

    var currentState by mutableStateOf<YouTubePlayerState>(YouTubePlayerState.Idle)
        private set

    internal var command by mutableStateOf<YouTubeExecCommand?>(null)
    private var continuation: CancellableContinuation<Unit>? = null

    suspend fun loadVideo(videoId: YouTubeVideoId) =
        executeCommand(YouTubeExecCommand.LoadVideo(videoId))

    suspend fun play() = executeCommand(YouTubeExecCommand.Play)
    suspend fun pause() = executeCommand(YouTubeExecCommand.Pause)
    suspend fun seekTo(duration: Duration) = executeCommand(YouTubeExecCommand.SeekTo(duration))
    suspend fun seekBy(duration: Duration) = executeCommand(YouTubeExecCommand.SeekBy(duration))
    suspend fun mute() = executeCommand(YouTubeExecCommand.Mute)
    suspend fun unMute() = executeCommand(YouTubeExecCommand.Unmute)
    suspend fun setVolume(volume: Int) = executeCommand(YouTubeExecCommand.SetVolume(volume))
    suspend fun setPlaybackRate(rate: Float) =
        executeCommand(YouTubeExecCommand.SetPlaybackRate(rate))

    suspend fun toggleFullScreen() = executeCommand(YouTubeExecCommand.ToggleFullscreen)

    suspend fun executeCommand(
        command: YouTubeExecCommand
    ): Unit = mutex.withLock {
        try {
            suspendCancellableCoroutine {
                (command as? YouTubeExecCommand.LoadVideo)?.also { loadVideo ->
                    currentState = YouTubePlayerState.Playing(loadVideo.videoId)
                }
                this.continuation = it
                this.command = command
            }
        } finally {
            this.command = null
            this.continuation = null
        }
    }


    internal fun complete() {
        continuation?.resume(Unit)
    }

    internal fun updateState(event: YouTubeEvent) {
        val state = when (event) {
            is YouTubeEvent.Error -> YouTubePlayerState.Error(event.error)
            is YouTubeEvent.OnVideoIdHandled -> YouTubePlayerState.Playing(event.videoId)
            is YouTubeEvent.PlaybackQualityChange -> when (val state = currentState) {
                is YouTubePlayerState.Playing -> state.copy(quality = event.quality)
                else -> YouTubePlayerState.Error("Incorrect player state. Expected Playing, but was $state")
            }

            is YouTubeEvent.RateChange -> {
                // Do nothing
                currentState
            }

            YouTubeEvent.Ready -> when (val state = currentState) {
                is YouTubePlayerState.Playing -> state
                else -> YouTubePlayerState.Ready
            }

            is YouTubeEvent.StateChanged -> when (val state = currentState) {
                is YouTubePlayerState.Playing -> state.copy(
                    isPlaying = event.state == YouTubeEvent.StateChanged.State.PLAYING
                )

                else -> YouTubePlayerState.Error("Incorrect player state. Expected Playing, but was $state")
            }

            is YouTubeEvent.TimeChanged -> when (val state = currentState) {
                is YouTubePlayerState.Playing -> state.copy(
                    currentTime = event.time,
                )

                else -> YouTubePlayerState.Error("Incorrect player state. Expected Playing, but was $state")
            }

            is YouTubeEvent.VideoDuration -> when (val state = currentState) {
                is YouTubePlayerState.Playing -> state.copy(
                    duration = event.duration,
                )

                else -> YouTubePlayerState.Error("Incorrect player state. Expected Playing, but was $state")
            }
        }
        currentState = state
    }
}
