package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.handler.YouTubeActionHandler
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeEvent
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider.ConstantHTMLContentProvider
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider.HTMLContentProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow

private const val BASE_URL = "https://www.youtube.com"
private const val PLAYER_VARS_KEY = "<<injectedPlayerVars>>"

private val htmlContentProvider: HTMLContentProvider = ConstantHTMLContentProvider()

@Composable
fun YouTubePlayer(
    modifier: Modifier = Modifier,
    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
    execCommandFlow: Flow<YouTubeExecCommand?> = emptyFlow(),
    youTubeActionListener: ((YouTubeEvent) -> Unit)? = null,
) = YouTubePlayer(
    modifier = modifier,
    options = options,
    execCommandState = execCommandFlow
        .distinctUntilChanged()
        .collectAsState(initial = null),
    youTubeActionListener = youTubeActionListener,
)

@Composable
fun YouTubePlayer(
    modifier: Modifier = Modifier,
    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
    execCommandState: State<YouTubeExecCommand?>,
    youTubeActionListener: ((YouTubeEvent) -> Unit)? = null,
) = YouTubePlayer(
    modifier = modifier,
    options = options,
    execCommand = execCommandState.value,
    youTubeActionListener = youTubeActionListener,
)

@Composable
fun YouTubePlayer(
    modifier: Modifier = Modifier,
    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
    execCommand: YouTubeExecCommand? = null,
    youTubeActionListener: ((YouTubeEvent) -> Unit)? = null,
) {
    val htmlContent: String = remember(options) {
        htmlContentProvider.provideHTMLContent()
            .replace(PLAYER_VARS_KEY, options.build())
    }
    val webViewState = rememberWebViewStateWithHTMLData(
        data = htmlContent,
        baseUrl = BASE_URL,
        mimeType = "text/html",
        encoding = "utf-8",
    )

    val navigator = rememberWebViewNavigator()
    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        androidWebSettings.apply {
            isAlgorithmicDarkeningAllowed = true
            safeBrowsingEnabled = false
            domStorageEnabled = true
            supportZoom = false
        }
    }

    YouTubePlayerExecutor(
        navigator = navigator,
        execCommand = execCommand,
    )

    youTubeActionListener?.also { safeListener ->
        val action = YouTubeActionHandler.handleAction(webViewState.pageTitle)
        if (action != null) {
            safeListener(action)
        }
    }
    WebView(
        modifier = modifier.fillMaxSize(),
        state = webViewState,
        navigator = navigator,
    )
}

@Composable
private fun YouTubePlayerExecutor(
    navigator: WebViewNavigator,
    execCommand: YouTubeExecCommand? = null,
) {
    LaunchedEffect(execCommand) {
        execCommand?.also { command ->
            //navigator.loadUrl("javascript:${command.command()}")
            navigator.evaluateJavaScript(command.command()) {
                println("webView. Command: $command Execution result: $it")
            }
        }
    }
}
