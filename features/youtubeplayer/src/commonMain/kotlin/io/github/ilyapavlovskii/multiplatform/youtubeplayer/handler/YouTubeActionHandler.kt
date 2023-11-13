package io.github.ilyapavlovskii.multiplatform.youtubeplayer.handler

import com.chrynan.uri.core.Uri
import com.chrynan.uri.core.fromStringOrNull
import com.chrynan.uri.core.queryParameters
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeAction
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeOperation

object YouTubeActionHandler {

    private const val SCHEME = "ytplayer"
    private const val DATA_KEY = "data"

    fun handleAction(url: String?): YouTubeAction? {
        val uri = url?.let { Uri.fromStringOrNull(uriString = it) } ?: return null
        return if (uri.scheme == SCHEME) {
            val operation = uri.host?.let(YouTubeOperation.Companion::fromStringOrNull)
            val data = uri.queryParameters()[DATA_KEY].orEmpty()
            println("webViewState. OPERATION_HANDLED: Path: ${uri.path} " +
                    "Operation: $operation " +
                    "Params: ${uri.queryParameters()} " +
                    "Data: ${uri.queryParameters()[DATA_KEY]}")
            YouTubeAction.fromStringOrNull(operation, data)
        } else {
            null
        }
    }
}