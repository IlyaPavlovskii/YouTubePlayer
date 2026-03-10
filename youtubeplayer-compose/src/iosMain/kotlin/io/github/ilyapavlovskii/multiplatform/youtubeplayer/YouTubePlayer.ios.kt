package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import androidx.compose.runtime.Composable
import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.PlatformWebViewParams
import com.multiplatform.webview.web.WebViewNavigator
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand
import kotlinx.cinterop.ExperimentalForeignApi
import platform.WebKit.WKUserScript
import platform.WebKit.WKUserScriptInjectionTime

internal actual fun executeCommand(
    navigator: WebViewNavigator,
    execCommand: YouTubeExecCommand
) {
    val command = if (execCommand is YouTubeExecCommand.ToggleFullscreen) {
        "toggleFullscreenIOS();"
    } else {
        execCommand.command()
    }
    navigator.evaluateJavaScript(command)
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun configureYouTubeWebView(webView: NativeWebView) {
    // Inject a script into ALL frames (forMainFrameOnly = false) so it also runs
    // inside the YouTube iframe. It listens for the 'ytplayer://toggleFullscreen'
    // postMessage sent from the main frame and calls webkitEnterFullscreen() on
    // the <video> element — the only fullscreen API available on iOS WKWebView iframes.
    val source = """
        window.addEventListener('message', function(event) {
            if (event.data !== 'ytplayer://toggleFullscreen') return;
            var video = document.querySelector('video');
            if (!video) return;
            if (video.webkitDisplayingFullscreen) {
                video.webkitExitFullscreen();
            } else {
                video.webkitEnterFullscreen();
            }
        });
    """.trimIndent()
    val userScript = WKUserScript(
        source = source,
        injectionTime = WKUserScriptInjectionTime.WKUserScriptInjectionTimeAtDocumentEnd,
        forMainFrameOnly = false,
    )
    webView.configuration.userContentController.addUserScript(userScript)
}

@Composable
internal actual fun rememberYouTubePlatformWebViewParams(): PlatformWebViewParams? = null