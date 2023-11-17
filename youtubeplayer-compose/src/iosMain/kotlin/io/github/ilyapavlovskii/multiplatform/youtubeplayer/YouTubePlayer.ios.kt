package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import com.multiplatform.webview.web.WebViewNavigator
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand

internal actual fun executeCommand(
    navigator: WebViewNavigator,
    execCommand: YouTubeExecCommand
) {
    navigator.evaluateJavaScript(execCommand.command())
}