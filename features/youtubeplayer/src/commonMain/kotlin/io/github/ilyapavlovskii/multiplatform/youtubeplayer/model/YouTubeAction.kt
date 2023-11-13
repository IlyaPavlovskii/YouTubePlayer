package io.github.ilyapavlovskii.multiplatform.youtubeplayer.model

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

sealed class YouTubeAction {
    data object Ready : YouTubeAction()
    data class PlaybackQualityChange(
        val quality: Quality
    ) : YouTubeAction() {
        enum class Quality(val value: String) {
            SMALL("small"),
            MEDIUM("medium"),
            LARGE("large"),
            HD720("hd720"),
            HD1080("hd1080"),
            HIGH_RESOLUTION("highres"),
            ;
        }

        companion object {
            fun fromStringOrNull(value: String): PlaybackQualityChange? =
                Quality.entries.firstOrNull { it.value == value }
                    ?.let(::PlaybackQualityChange)
        }
    }

    data class RateChange(
        val rate: Float,
    ) : YouTubeAction() {
        companion object {
            fun fromStringOrNull(value: String): RateChange? =
                value.toFloatOrNull()?.let(::RateChange)
        }
    }

    data class Error(
        val error: String,
    ) : YouTubeAction()

    data class VideoDuration(
        val duration: Duration,
    ) : YouTubeAction() {
        companion object {
            fun fromStringOrNull(value: String): VideoDuration? = value
                .toDoubleOrNull()
                ?.toDuration(DurationUnit.SECONDS)
                ?.let(::VideoDuration)
        }
    }

    data class StateChanged(
        val state: State,
    ): YouTubeAction() {
        enum class State(val value: String) {
            UNSTARTED("UNSTARTED"),
            ENDED("ENDED"),
            PLAYING("PLAYING"),
            PAUSED("PAUSED"),
            BUFFERING("BUFFERING"),
            CUED("CUED"),
            ;
        }
        companion object {
            fun fromStringOrNull(value: String): StateChanged? = State.entries
                .firstOrNull { it.value == value }
                ?.let(::StateChanged)
        }
    }

    data class TimeChanged(
        val time: Duration,
    ): YouTubeAction() {
        companion object {
            fun fromStringOrNull(value: String): TimeChanged? = value
                .toDoubleOrNull()
                ?.toDuration(DurationUnit.SECONDS)
                ?.let(::TimeChanged)
        }
    }

    companion object {
        internal fun fromStringOrNull(
            operation: YouTubeOperation?,
            data: String
        ): YouTubeAction? = when (operation) {
            YouTubeOperation.READY -> Ready
            YouTubeOperation.PLAYBACK_QUALITY_CHANGE -> PlaybackQualityChange.fromStringOrNull(data)
            YouTubeOperation.RATE_CHANGE -> RateChange.fromStringOrNull(data)
            YouTubeOperation.ERROR -> Error(data)
            YouTubeOperation.VIDEO_DURATION -> VideoDuration.fromStringOrNull(data)
            YouTubeOperation.STATE_CHANGE -> StateChanged.fromStringOrNull(data)
            YouTubeOperation.CURRENT_TIME_CHANGE -> TimeChanged.fromStringOrNull(data)
            null -> null
        }
    }
}