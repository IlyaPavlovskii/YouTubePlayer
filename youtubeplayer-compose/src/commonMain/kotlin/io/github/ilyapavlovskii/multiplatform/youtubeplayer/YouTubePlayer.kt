package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.handler.YouTubeActionHandler
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeEvent
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider.ConstantHTMLContentProvider
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider.HTMLContentProvider
import io.github.ilyapavlovskii.`youtubeplayer-compose`.generated.resources.Res
import io.github.ilyapavlovskii.`youtubeplayer-compose`.generated.resources.html_data
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

private const val BASE_URL = "https://www.youtube.com"
private const val BASE_MIME_TYPE = "text/html"
private const val BASE_ENCODING = "utf-8"
private const val PLAYER_VARS_KEY = "<<injectedPlayerVars>>"

private val htmlContentProvider: HTMLContentProvider = ConstantHTMLContentProvider()

/**
 * YouTube player composable player.
 *
 * @param modifier modifier for styling component
 * @param options player options builder. See [YouTubePlayerOptionsBuilder]
 * documentation for more details.
 * @param hostState host state to manage and execute commands. See [YouTubePlayerHostState]
 * @param actionListener listener for YouTube events. See [YouTubeEvent] documentation.
 * */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun YouTubePlayer(
    modifier: Modifier = Modifier,
    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
    hostState: YouTubePlayerHostState,
    actionListener: ((YouTubeEvent) -> Unit)? = null,
) {
    val htmlData = stringResource(Res.string.html_data)
    val htmlContent: String = remember(options) {
        htmlData.replace(PLAYER_VARS_KEY, options.build())
    }
    val webViewState = rememberWebViewStateWithHTMLData(
        data = htmlContent,
        baseUrl = BASE_URL,
        mimeType = BASE_MIME_TYPE,
        encoding = BASE_ENCODING,
    )

    val navigator = rememberWebViewNavigator()
    val command: YouTubeExecCommand? = hostState.command
    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        androidWebSettings.apply {
            isAlgorithmicDarkeningAllowed = true
            safeBrowsingEnabled = false
            domStorageEnabled = true
            supportZoom = false
        }
    }

    LaunchedEffect(command) {
        command?.also { command ->
            executeCommand(navigator, command)
            hostState.complete()
        }
    }

    YouTubeActionHandler.handleAction(webViewState.pageTitle)?.also { event ->
        hostState.updateState(event)
        actionListener?.invoke(event)
    }

    WebView(
        modifier = modifier.fillMaxSize(),
        state = webViewState,
        navigator = navigator,
    )
}

internal expect fun executeCommand(
    navigator: WebViewNavigator,
    execCommand: YouTubeExecCommand,
)
