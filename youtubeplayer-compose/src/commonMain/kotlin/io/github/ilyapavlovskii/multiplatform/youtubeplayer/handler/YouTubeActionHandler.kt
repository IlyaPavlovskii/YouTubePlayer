package io.github.ilyapavlovskii.multiplatform.youtubeplayer.handler

import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeEvent
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeOperation

object YouTubeActionHandler {

    private val REGEX = "ytplayer://([A-z]+)(\\?data=([A-z\\d.]+))*".toRegex()

    fun handleAction(url: String?): YouTubeEvent? {
        val result = REGEX.matchEntire(url.orEmpty())
        return result?.let { matchResult ->
            val operation = matchResult.groupValues[1].let(YouTubeOperation.Companion::fromStringOrNull)
            val data = matchResult.groupValues[3]
            YouTubeEvent.fromStringOrNull(operation, data)
        }
    }
}
