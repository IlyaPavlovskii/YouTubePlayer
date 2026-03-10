package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.WebViewNavigator
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand

private const val COMMAND_EXECUTOR_PATTERN = "javascript:%s"

internal actual fun executeCommand(
    navigator: WebViewNavigator,
    execCommand: YouTubeExecCommand
) {
    navigator.loadUrl(COMMAND_EXECUTOR_PATTERN.format(execCommand.command()))
}

internal actual fun configureYouTubeWebView(webView: NativeWebView) {
    // No additional configuration needed on Android.
    // Fullscreen is handled via WebChromeClient.onShowCustomView.
}