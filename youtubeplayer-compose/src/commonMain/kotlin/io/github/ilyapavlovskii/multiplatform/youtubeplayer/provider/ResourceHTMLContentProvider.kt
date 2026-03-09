package io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider

import io.github.ilyapavlovskii.youtubeplayer_compose.generated.resources.Res

internal class ResourceHTMLContentProvider : HTMLContentProvider {
    override suspend fun provideHTMLContent(): String {
        return Res.readBytes("files/youtube_player.html").decodeToString()
    }
}
