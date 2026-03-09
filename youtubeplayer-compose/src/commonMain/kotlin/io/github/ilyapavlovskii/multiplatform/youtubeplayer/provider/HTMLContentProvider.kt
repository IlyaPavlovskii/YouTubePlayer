package io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider

internal interface HTMLContentProvider {
    suspend fun provideHTMLContent(): String
}