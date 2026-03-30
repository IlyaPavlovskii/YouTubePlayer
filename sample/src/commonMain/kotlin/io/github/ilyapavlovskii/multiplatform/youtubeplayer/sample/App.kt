package io.github.ilyapavlovskii.multiplatform.youtubeplayer.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.SimpleYouTubePlayerOptionsBuilder
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayer
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayerHostState
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayerState
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubeVideoId
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .background(Color(0xFF0F0F0F)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val coroutineScope = rememberCoroutineScope()
            val hostState = remember { YouTubePlayerHostState() }
            var videoDuration: String by remember { mutableStateOf("00:00") }
            var currentTime: String by remember { mutableStateOf("00:00") }
            var volume: Int by remember { mutableStateOf(100) }
            val errorLog = remember { mutableStateListOf<Pair<String, String>>() }

            LaunchedEffect(hostState) {
                hostState.commandError.collect { error ->
                    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    val timestamp = "${now.hour.toString().padStart(2, '0')}:" +
                        "${now.minute.toString().padStart(2, '0')}:" +
                        "${now.second.toString().padStart(2, '0')}"
                    errorLog.add(0, timestamp to error)
                }
            }

            val isPlaying = (hostState.currentState as? YouTubePlayerState.Playing)?.isPlaying == true

            when (val state = hostState.currentState) {
                is YouTubePlayerState.Error -> println("Error: ${state.message}")
                YouTubePlayerState.Idle -> {}
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
                    .height(220.dp),
                hostState = hostState,
                options = SimpleYouTubePlayerOptionsBuilder.builder {
                    autoplay(true)
                    mute(false)
                    controls(true)
                    fullscreen(true)
                    rel(false)
                    ivLoadPolicy(false)
                    ccLoadPolicy(false)
                    enableJsApi(true)
                    fullscreen = true
                },
            )

            // Remote control body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF1C1C1C))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Utility row: Mute | Unmute | Fullscreen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    RemoteButton(
                        icon = PlayerIcons.VolumeOff,
                        contentDescription = "Mute",
                        size = 52.dp,
                    ) {
                        coroutineScope.launch { hostState.mute() }
                    }
                    RemoteButton(
                        icon = PlayerIcons.VolumeUp,
                        contentDescription = "Unmute",
                        size = 52.dp,
                    ) {
                        coroutineScope.launch { hostState.unMute() }
                    }
                    RemoteButton(
                        icon = PlayerIcons.Fullscreen,
                        contentDescription = "Fullscreen",
                        size = 52.dp,
                    ) {
                        coroutineScope.launch { hostState.toggleFullScreen() }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // D-pad top: Seek -10s
                RemoteButton(
                    icon = PlayerIcons.FastRewind,
                    contentDescription = "Seek back 10 seconds",
                ) {
                    coroutineScope.launch { hostState.seekBy((-10).seconds) }
                }

                // D-pad middle: Vol- | Play/Pause | Vol+
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RemoteButton(
                        icon = PlayerIcons.VolumeDown,
                        contentDescription = "Volume down",
                    ) {
                        volume = (volume - 10).coerceAtLeast(0)
                        coroutineScope.launch { hostState.setVolume(volume) }
                    }
                    RemoteButton(
                        icon = if (isPlaying) PlayerIcons.Pause else PlayerIcons.Play,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        size = 80.dp,
                        backgroundColor = Color(0xFFFF0000),
                    ) {
                        coroutineScope.launch {
                            if (isPlaying) hostState.pause() else hostState.play()
                        }
                    }
                    RemoteButton(
                        icon = PlayerIcons.VolumeUp,
                        contentDescription = "Volume up",
                    ) {
                        volume = (volume + 10).coerceAtMost(100)
                        coroutineScope.launch { hostState.setVolume(volume) }
                    }
                }

                // D-pad bottom: Seek +10s
                RemoteButton(
                    icon = PlayerIcons.FastForward,
                    contentDescription = "Seek forward 10 seconds",
                ) {
                    coroutineScope.launch { hostState.seekBy(10.seconds) }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Time display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = currentTime,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = videoDuration,
                        color = Color(0xFF888888),
                        fontSize = 13.sp,
                    )
                }
            }

            // Error log
            if (errorLog.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1C1C1C))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Errors",
                        color = Color(0xFFFF6B6B),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    LazyColumn(modifier = Modifier.heightIn(max = (5 * 28).dp)) {
                        items(errorLog) { (timestamp, message) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(28.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = timestamp,
                                    color = Color(0xFF888888),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                                Text(
                                    text = message,
                                    color = Color(0xFFFF6B6B),
                                    fontSize = 11.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(duration: Duration): String {
    val seconds = duration.inWholeSeconds
    val minutes = seconds / 60
    return "${minutes % 60}:${(seconds % 60).toString().padStart(2, '0')}"
}

fun Modifier.gesturesDisabled(disabled: Boolean = true) =
    if (disabled) {
        pointerInput(Unit) {
            awaitPointerEventScope {
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
