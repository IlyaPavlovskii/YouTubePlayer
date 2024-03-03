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
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.SimpleYouTubePlayerOptionsBuilder
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayer
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayerHostState
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayerState
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubeVideoId
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeEvent
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.model.YouTubeExecCommand
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val coroutineScope = rememberCoroutineScope()
            val hostState = remember { YouTubePlayerHostState() }
            var videoDuration: String by remember { mutableStateOf("00:00") }
            var currentTime: String by remember { mutableStateOf("00:00") }

            when(val state = hostState.currentState) {
                is YouTubePlayerState.Error -> {
                    Text(text = "Error: ${state.message}")
                }
                YouTubePlayerState.Idle -> {
                    // Do nothing, waiting for initialization
                }
                is YouTubePlayerState.Playing -> {
                    videoDuration = formatTime(state.duration)
                    currentTime = formatTime(state.currentTime)
                }
                YouTubePlayerState.Ready -> coroutineScope.launch {
                    hostState.loadVideo(YouTubeVideoId("ufKj1sBrC4Q"))
                }
            }

            YouTubePlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .gesturesDisabled(),
                hostState = hostState,
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
                    coroutineScope.launch { hostState.play() }
                }
                SimpleButton(text = "Pause") {
                    coroutineScope.launch { hostState.pause() }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                SimpleButton(text = "Seek by -10s") {
                    coroutineScope.launch { hostState.seekBy((-10).seconds) }
                }
                SimpleButton(text = "Seek by +10s") {
                    coroutineScope.launch { hostState.seekBy(10.seconds) }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                SimpleButton(text = "Mute") {
                    coroutineScope.launch { hostState.mute() }
                }
                SimpleButton(text = "Unmute") {
                    coroutineScope.launch { hostState.unMute() }
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