package io.github.ilyapavlovskii.multiplatform.youtubeplayer.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.SimpleYouTubePlayerOptionsBuilder
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayer
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubeVideoId

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
                .height(300.dp)
                .background(Color.Yellow),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            YouTubePlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                videoId = YouTubeVideoId("GbfeH6Q8PzY"),
                options = SimpleYouTubePlayerOptionsBuilder.builder {
                    autoplay(false)
                    controls(false)
                    rel(false)
                    ivLoadPolicy(false)
                    ccLoadPolicy(false)
                },
            )
        }
    }
}