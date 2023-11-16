package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow

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
 * @param execCommandFlow flow of commands to execute. See [YouTubeExecCommand]
 * @param actionListener listener for YouTube events. See [YouTubeEvent] documentation.
 * */
@Composable
fun YouTubePlayer(
    modifier: Modifier = Modifier,
    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
    execCommandFlow: Flow<YouTubeExecCommand?> = emptyFlow(),
    actionListener: ((YouTubeEvent) -> Unit)? = null,
) = YouTubePlayer(
    modifier = modifier,
    options = options,
    execCommandState = execCommandFlow
        .distinctUntilChanged()
        .collectAsState(initial = null),
    actionListener = actionListener,
)

/**
 * YouTube player composable player.
 *
 * @param modifier modifier for styling component
 * @param options player options builder. See [YouTubePlayerOptionsBuilder]
 * documentation for more details.
 * @param execCommandState state of commands to execute. See [YouTubeExecCommand]
 * @param actionListener listener for YouTube events. See [YouTubeEvent] documentation.
 * */
@Composable
fun YouTubePlayer(
    modifier: Modifier = Modifier,
    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
    execCommandState: State<YouTubeExecCommand?>,
    actionListener: ((YouTubeEvent) -> Unit)? = null,
) = YouTubePlayer(
    modifier = modifier,
    options = options,
    execCommand = execCommandState.value,
    actionListener = actionListener,
)

/**
 * YouTube player composable player.
 *
 * @param modifier modifier for styling component
 * @param options player options builder. See [YouTubePlayerOptionsBuilder]
 * documentation for more details.
 * @param execCommand command to execute. See [YouTubeExecCommand]
 * @param actionListener listener for YouTube events. See [YouTubeEvent] documentation.
 * */
@Composable
fun YouTubePlayer(
    modifier: Modifier = Modifier,
    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
    execCommand: YouTubeExecCommand? = null,
    actionListener: ((YouTubeEvent) -> Unit)? = null,
) {
    val htmlContent: String = remember(options) {
        htmlContentProvider.provideHTMLContent()
            .replace(PLAYER_VARS_KEY, options.build())
    }
    val webViewState = rememberWebViewStateWithHTMLData(
        data = htmlContent,
        baseUrl = BASE_URL,
        mimeType = BASE_MIME_TYPE,
        encoding = BASE_ENCODING,
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

    actionListener?.also { safeListener ->
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
        execCommand?.also { command -> executeCommand(navigator, command) }
    }
}

internal expect fun executeCommand(
    navigator: WebViewNavigator,
    execCommand: YouTubeExecCommand,
)
