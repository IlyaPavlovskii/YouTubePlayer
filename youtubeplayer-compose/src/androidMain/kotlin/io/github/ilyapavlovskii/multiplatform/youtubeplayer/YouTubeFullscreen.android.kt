package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import android.view.View
import android.webkit.WebChromeClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.multiplatform.webview.web.AccompanistWebChromeClient
import com.multiplatform.webview.web.PlatformWebViewParams

@Composable
internal actual fun rememberYouTubePlatformWebViewParams(): PlatformWebViewParams? {
    val customViewState = remember { mutableStateOf<View?>(null) }
    val customViewCallbackState = remember { mutableStateOf<WebChromeClient.CustomViewCallback?>(null) }

    if (customViewState.value != null) {
        Dialog(
            onDismissRequest = {
                customViewCallbackState.value?.onCustomViewHidden()
                customViewState.value = null
                customViewCallbackState.value = null
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
            ),
        ) {
            AndroidView(
                factory = { customViewState.value!! },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
            )
        }
    }

    val chromeClient = remember {
        object : AccompanistWebChromeClient() {
            override fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
                customViewState.value = view
                customViewCallbackState.value = callback
            }

            override fun onHideCustomView() {
                customViewState.value = null
                customViewCallbackState.value = null
            }
        }
    }

    return remember(chromeClient) { PlatformWebViewParams(chromeClient = chromeClient) }
}
