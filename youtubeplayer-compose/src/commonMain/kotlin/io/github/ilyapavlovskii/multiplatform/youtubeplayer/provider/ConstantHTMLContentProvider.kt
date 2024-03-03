package io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider

import io.github.ilyapavlovskii.`youtubeplayer-compose`.generated.resources.Res
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi

internal class ConstantHTMLContentProvider : HTMLContentProvider {

    @OptIn(ExperimentalResourceApi::class)
    override fun provideHTMLContent(): String = runBlocking {
        Res.readBytes("files/html-content.html").decodeToString()
    }
}