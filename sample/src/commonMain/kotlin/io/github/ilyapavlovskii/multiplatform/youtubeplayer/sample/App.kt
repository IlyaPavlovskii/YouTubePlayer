package io.github.ilyapavlovskii.multiplatform.youtubeplayer.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.SimpleYouTubePlayerOptionsBuilder
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayer
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubeVideoId
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeAction
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
                .height(300.dp)
                .background(Color.Yellow),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var execCommand: YouTubeExecCommand? by remember { mutableStateOf(null) }
            YouTubePlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                execCommand = execCommand,
                youTubeActionListener = { action ->
                    println("webViewState. ACTION HANDlED: $action")
                    when (action) {
                        YouTubeAction.Ready -> {
                            execCommand = YouTubeExecCommand.LoadVideo(YouTubeVideoId("GbfeH6Q8PzY"))
                        }
                        is YouTubeAction.Error,
                        is YouTubeAction.PlaybackQualityChange,
                        is YouTubeAction.RateChange,
                        is YouTubeAction.StateChanged,
                        is YouTubeAction.TimeChanged,
                        is YouTubeAction.VideoDuration -> {
                            println("webViewState. onAction HANDlED: $action")
                        }
                    }
                },
                options = SimpleYouTubePlayerOptionsBuilder.builder {
                    autoplay(true)
                    controls(false)
                    rel(false)
                    ivLoadPolicy(false)
                    ccLoadPolicy(false)
                },
            )
        }
    }
}