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

    data class Composite(
        val commands: List<YouTubeExecCommand>,
    ): YouTubeExecCommand {
        constructor(vararg commands: YouTubeExecCommand) : this(commands.toList())

        override fun command(): String = commands.joinToString(separator = "\n")
    }
}
