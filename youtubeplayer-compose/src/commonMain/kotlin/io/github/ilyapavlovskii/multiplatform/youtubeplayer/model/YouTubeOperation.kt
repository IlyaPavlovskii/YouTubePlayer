package io.github.ilyapavlovskii.multiplatform.youtubeplayer.model

internal enum class YouTubeOperation(val operationName: String) {
    READY("onReady"),
    PLAYBACK_QUALITY_CHANGE("onPlaybackQualityChange"),
    RATE_CHANGE("onPlaybackRateChange"),
    ERROR("onError"),
    VIDEO_DURATION("onVideoDuration"),
    STATE_CHANGE("onStateChange"),
    CURRENT_TIME_CHANGE("onCurrentTimeChange"),
    ON_VIDEO_ID_HANDLED("onVideoId")
    ;

    companion object {
        fun fromStringOrNull(path: String): YouTubeOperation? {
            return entries.firstOrNull { it.operationName == path }
        }
    }
}