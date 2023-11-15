package io.github.ilyapavlovskii.multiplatform.youtubeplayer.model

import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubeVideoId
import kotlin.time.Duration

sealed interface YouTubeExecCommand {
    fun command(): String

    data class LoadVideo(
        val videoId: YouTubeVideoId,
        val startSeconds: Duration = Duration.ZERO,
    ) : YouTubeExecCommand {
        override fun command(): String =
            "loadVideo(\'${videoId.id}\', ${startSeconds.inWholeSeconds});"
    }

    data object Play : YouTubeExecCommand {
        override fun command(): String = "playVideo();"
    }

    data object Pause : YouTubeExecCommand {
        override fun command(): String = "pauseVideo();"
    }

    data class SeekTo(val duration: Duration): YouTubeExecCommand {
        override fun command(): String = "seekTo(${duration.inWholeSeconds});"
    }

    data class SeekBy(val duration: Duration): YouTubeExecCommand {
        override fun command(): String = "seekBy(${duration.inWholeSeconds});"
    }

    data class CueVideo(
        val videoId: YouTubeVideoId,
        val startSeconds: Duration = Duration.ZERO,
    ) : YouTubeExecCommand {
        override fun command(): String =
            "cueVideo(\'${videoId.id}\', ${startSeconds.inWholeSeconds});"
    }

    data object Mute : YouTubeExecCommand {
        override fun command(): String = "mute();"
    }
    data object Unmute : YouTubeExecCommand {
        override fun command(): String = "unMute();"
    }
    data class SetVolume(val volumePercent: Int) : YouTubeExecCommand {
        override fun command(): String = "setVolume(${volumePercent});"
    }
    data class SetPlaybackRate(val playbackRate: Float) : YouTubeExecCommand {
        override fun command(): String = "setPlaybackRate(${playbackRate});"
    }
    data object ToggleFullscreen : YouTubeExecCommand {
        override fun command(): String = "toggleFullscreen();"
    }
    data object NextVideo : YouTubeExecCommand {
        override fun command(): String = "nextVideo();"
    }
    data object PreviousVideo : YouTubeExecCommand {
        override fun command(): String = "previousVideo();"
    }
    data class PlayVideoAt(val index: Int) : YouTubeExecCommand {
        override fun command(): String = "playVideoAt(${index});"
    }
    data class SetLoop(val loop: Boolean) : YouTubeExecCommand {
        override fun command(): String = "setLoop(${loop});"
    }
    data class SetShuffle(val shuffle: Boolean) : YouTubeExecCommand {
        override fun command(): String = "setShuffle(${shuffle});"
    }
}
