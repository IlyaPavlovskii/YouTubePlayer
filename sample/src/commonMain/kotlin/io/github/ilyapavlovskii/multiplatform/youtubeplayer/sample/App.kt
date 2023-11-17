package io.github.ilyapavlovskii.multiplatform.youtubeplayer.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
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
                    .height(300.dp)
                    .gesturesDisabled(),
                execCommandState = execCommand,
                actionListener = { action ->
                    when (action) {
                        YouTubeEvent.Ready -> {
                            execCommand.value = YouTubeExecCommand.LoadVideo(
                                videoId = YouTubeVideoId("ufKj1sBrC4Q"),
                            )
                        }

                        is YouTubeEvent.VideoDuration -> {
                            videoDuration = formatTime(action.duration)
                        }

                        is YouTubeEvent.TimeChanged -> {
                            currentTime = formatTime(action.time)
                        }

                        is YouTubeEvent.OnVideoIdHandled,
                        is YouTubeEvent.Error,
                        is YouTubeEvent.PlaybackQualityChange,
                        is YouTubeEvent.RateChange,
                        is YouTubeEvent.StateChanged,
                        -> println("webViewState. onAction HANDlED: $action")
                    }
                },
                options = SimpleYouTubePlayerOptionsBuilder.builder {
                    autoplay(true)
                    controls(false)
                    rel(false)
                    ivLoadPolicy(false)
                    ccLoadPolicy(false)
                    fullscreen = true
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                SimpleButton(text = "Play") {
                    execCommand.value = YouTubeExecCommand.Play
                }
                SimpleButton(text = "Pause") {
                    execCommand.value = YouTubeExecCommand.Pause
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                SimpleButton(text = "Seek by -10s") {
                    execCommand.value = YouTubeExecCommand.SeekBy((-10).seconds)
                }
                SimpleButton(text = "Seek by +10s") {
                    execCommand.value = YouTubeExecCommand.SeekBy(10.seconds)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                SimpleButton(text = "Mute") {
                    execCommand.value = YouTubeExecCommand.Mute
                }
                SimpleButton(text = "Unmute") {
                    execCommand.value = YouTubeExecCommand.Unmute
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
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

fun Modifier.gesturesDisabled(disabled: Boolean = true) =
    if (disabled) {
        pointerInput(Unit) {
            awaitPointerEventScope {
                // we should wait for all new pointer events
                while (true) {
                    awaitPointerEvent(pass = PointerEventPass.Initial)
                        .changes
                        .forEach(PointerInputChange::consume)
                }
            }
        }
    } else {
        this
    }