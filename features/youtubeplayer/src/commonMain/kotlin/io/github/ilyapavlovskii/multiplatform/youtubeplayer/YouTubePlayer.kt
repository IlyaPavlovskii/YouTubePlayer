package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider.ConstantHTMLContentProvider
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider.HTMLContentProvider

private const val PLAYER_VARS_KEY = "<<injectedPlayerVars>>"

private val htmlContentProvider: HTMLContentProvider = ConstantHTMLContentProvider()

@Composable
fun YouTubePlayer(
    modifier: Modifier = Modifier,
    videoId: YouTubeVideoId,
    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
) {

    val htmlContent: String = htmlContentProvider.provideHTMLContent()
        .replace(PLAYER_VARS_KEY, options.build())

    val webViewState = rememberWebViewStateWithHTMLData(data = htmlContent)
    val navigator = rememberWebViewNavigator()

    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        androidWebSettings.apply {
            isAlgorithmicDarkeningAllowed = true
            safeBrowsingEnabled = true
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        WebView(
            modifier = modifier.fillMaxSize(),
            state = webViewState,
            navigator = navigator,
            captureBackPresses = false,
        )
    }

}