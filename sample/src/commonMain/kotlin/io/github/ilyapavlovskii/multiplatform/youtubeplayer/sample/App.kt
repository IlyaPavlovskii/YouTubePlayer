package io.github.ilyapavlovskii.multiplatform.youtubeplayer.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.SimpleYouTubePlayerOptionsBuilder
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayer
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubeVideoId
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeEvent
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val execCommand = mutableStateOf<YouTubeExecCommand?>(null)
            var videoDuration: String by remember { mutableStateOf("00:00") }
            var currentTime: String by remember { mutableStateOf("00:00") }
            YouTubePlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                execCommandState = execCommand,
                youTubeActionListener = { action ->
                    println("webViewState. ACTION HANDlED: $action")
                    when (action) {
                        YouTubeEvent.Ready -> {
                            execCommand.value = YouTubeExecCommand.LoadVideo(
                                videoId = YouTubeVideoId("GbfeH6Q8PzY"),
                            )
                        }

                        is YouTubeEvent.VideoDuration -> {
                            videoDuration = formatTime(action.duration)
                        }

                        is YouTubeEvent.TimeChanged -> {
                            currentTime = formatTime(action.time)
                        }
                        is YouTubeEvent.OnVideoIdHandled -> {
                            //execCommand.value = YouTubeExecCommand.Unmute
                        }

                        is YouTubeEvent.Error,
                        is YouTubeEvent.PlaybackQualityChange,
                        is YouTubeEvent.RateChange,
                        is YouTubeEvent.StateChanged,
                        -> {
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
                    mute(true)
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = {
                        execCommand.value = YouTubeExecCommand.Play
                    },
                ) {
                    Text(text = "Play")
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = {
                        execCommand.value = YouTubeExecCommand.Pause
                    },
                ) {
                    Text(text = "Pause")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = {
                        execCommand.value = YouTubeExecCommand.SeekBy((-10).seconds)
                    },
                ) {
                    Text(text = "Seek to -10s")
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = {
                        execCommand.value = YouTubeExecCommand.SeekBy(10.seconds)
                    },
                ) {
                    Text(text = "Seek to +10s")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "00:00",
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = currentTime,
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = videoDuration,
                )
            }
        }
    }
}

private fun formatTime(duration: Duration): String {
    val seconds = duration.inWholeSeconds
    val minutes = seconds / 60
    return "${minutes % 60}:${seconds % 60}"
}