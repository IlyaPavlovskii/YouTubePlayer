package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.handler.YouTubeActionHandler
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeAction
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider.ConstantHTMLContentProvider
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider.HTMLContentProvider

private const val BASE_URL = "https://www.youtube.com"
private const val PLAYER_VARS_KEY = "<<injectedPlayerVars>>"

private val htmlContentProvider: HTMLContentProvider = ConstantHTMLContentProvider()

//@Composable
//fun YouTubePlayer(
//    modifier: Modifier = Modifier,
//    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
//    //execCommand: Flow<YouTubeExecCommand?>,
//    youTubeActionListener: YouTubeActionListener? = null,
//) {
//    YouTubePlayer(
//        modifier = modifier,
//        options = options,
//        execCommandState = execCommand
//            .distinctUntilChanged()
//            .collectAsState(initial = null),
//        youTubeActionListener = youTubeActionListener,
//    )
//}

@Composable
fun YouTubePlayer(
    modifier: Modifier = Modifier,
    options: YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder(),
    execCommand: YouTubeExecCommand? = null,
    youTubeActionListener: ((YouTubeAction) -> Unit)? = null,
) {
    val htmlContent: String = remember(options) {
        htmlContentProvider.provideHTMLContent()
            .replace(PLAYER_VARS_KEY, options.build())
    }
    val webViewState = rememberWebViewStateWithHTMLData(
        data = htmlContent,
        baseUrl = BASE_URL,
    )
    val navigator = rememberWebViewNavigator()

    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        androidWebSettings.apply {
            isAlgorithmicDarkeningAllowed = true
            safeBrowsingEnabled = true
            domStorageEnabled = true
        }
    }

    youTubeActionListener?.also { safeListener ->
        val action = YouTubeActionHandler.handleAction(webViewState.pageTitle)
        println("webViewState. YouTubeActionHandler. TITLE: ${webViewState.pageTitle} Action: $action")
        if (action != null) {
            safeListener(action)
        }
    }
    LaunchedEffect(execCommand) {
        println("\twebViewState. execCommandStateValue: $execCommand")
        when(execCommand) {
            is YouTubeExecCommand.Composite -> execCommand.command().also { safeCommand ->
                println("webViewState. EXEC_COMMAND comp: $safeCommand")
                navigator.evaluateJavaScript(safeCommand)
            }
            else -> execCommand?.command()?.also { safeCommand ->
                println("webViewState. EXEC_COMMAND: $safeCommand")
                navigator.evaluateJavaScript(safeCommand)
            }
        }
    }
    WebView(
        modifier = modifier.fillMaxSize(),
        state = webViewState,
        navigator = navigator,
    )
}
