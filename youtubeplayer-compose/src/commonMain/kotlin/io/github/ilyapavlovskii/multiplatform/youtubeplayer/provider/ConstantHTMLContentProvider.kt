package io.github.ilyapavlovskii.multiplatform.youtubeplayer.provider

internal class ConstantHTMLContentProvider : HTMLContentProvider {

    override fun provideHTMLContent(): String = HTML_CONTENT.trimIndent()

    private companion object {
        const val HTML_CONTENT = """
<!DOCTYPE html>
<html>
<style type="text/css">
        html, body {
            height: 100%;
            width: 100%;
            margin: 0;
            padding: 0;
            background-color: #000000;
            overflow: hidden;
            position: fixed;
        }
    </style>

  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!-- defer forces the library to execute after the html page is fully parsed. -->
    <!-- This is needed to avoid race conditions, where the library executes and calls `onYouTubeIframeAPIReady` before the page is fully parsed. -->
    <!-- See #873 on GitHub -->
    <script defer src="https://www.youtube.com/iframe_api"></script>
  </head>

  <body>
    <div id="youTubePlayerDOM"></div>
  </body>

<script type="text/javascript">
var UNSTARTED = "UNSTARTED";
var ENDED = "ENDED";
var PLAYING = "PLAYING";
var PAUSED = "PAUSED";
var BUFFERING = "BUFFERING";
var CUED = "CUED";

var player;
var timerId;

function onYouTubeIframeAPIReady() {

  document.title = 'ytplayer://onYouTubeIframeAPIReady';

    player = new YT.Player('youTubePlayerDOM', {

    height: '100%',
    width: '100%',
    
    events: {
        onReady: function(event) {
             document.title = 'ytplayer://onReady';
        },
        onStateChange: function(event) {
            sendPlayerStateChange(event.data);
        },
        onPlaybackQualityChange: function(event) {
            document.title = 'ytplayer://onPlaybackQualityChange?data='+event.data;
        },
        onPlaybackRateChange: function(event) {
            document.title = 'ytplayer://onPlaybackRateChange?data='+event.data;
        },
        onError: function(error) {
            document.title = 'ytplayer://onError?data='+event.data;
        }
      },
      
      playerVars: <<injectedPlayerVars>>
  });
}

function sendPlayerStateChange(playerState) {
  clearTimeout(timerId);

  switch (playerState) {
    case YT.PlayerState.UNSTARTED:
      sendStateChange(UNSTARTED);
      sendVideoIdFromPlaylistIfAvailable(player);
      return;

    case YT.PlayerState.ENDED:
      sendStateChange(ENDED);
      return;

    case YT.PlayerState.PLAYING:
      sendStateChange(PLAYING);

      startSendCurrentTimeInterval();
      sendVideoData(player);
      return;

    case YT.PlayerState.PAUSED:
      sendStateChange(PAUSED);
      return;

    case YT.PlayerState.BUFFERING:
      sendStateChange(BUFFERING);
      return;

    case YT.PlayerState.CUED:
      sendStateChange(CUED);
      return;
  }

  function sendVideoData(player) {
    var videoDuration = player.getDuration();
    document.title = 'ytplayer://onVideoDuration?data='+videoDuration;
  }

  function sendVideoIdFromPlaylistIfAvailable(player) {
    var playlist = player.getPlaylist();
    if ( typeof playlist !== 'undefined' && Array.isArray(playlist) && playlist.length > 0 ) {
      var index = player.getPlaylistIndex();
      var videoId = playlist[index];
       document.title = 'ytplayer://onVideoId?data='+videoId;
    }
  }

  function sendStateChange(newState) {
    document.title = 'ytplayer://onStateChange?data='+newState;
  }

  function startSendCurrentTimeInterval() {
    timerId = setInterval(function() {
        document.title = 'ytplayer://onCurrentTimeChange?data='+player.getCurrentTime();
    }, 100 );
  }
}

function seekTo(startSeconds) {
  player.seekTo(startSeconds, true);
  document.title = 'ytplayer://onCurrentTimeChange?data='+startSeconds;
}

function seekBy(startSeconds) {
  seekTo(player.getCurrentTime()+startSeconds);
}

function pauseVideo() {
  player.pauseVideo();
}

function playVideo() {
  player.playVideo();
}

function loadVideo(videoId, startSeconds) {
  player.loadVideoById(videoId, startSeconds);
  document.title = 'ytplayer://onVideoId?data='+videoId;
}

function cueVideo(videoId, startSeconds) {
  player.cueVideoById(videoId, startSeconds);
  document.title = 'ytplayer://onVideoId?data='+videoId;
}

function mute() {
  player.mute();
}

function unMute() {
  player.unMute();
}

function setVolume(volumePercent) {
  player.setVolume(volumePercent);
}

function setPlaybackRate(playbackRate) {
  player.setPlaybackRate(playbackRate);
}

function toggleFullscreen() {
  player.toggleFullscreen();
}

function nextVideo() {
  player.nextVideo();
}

function previousVideo() {
  player.previousVideo();
}

function playVideoAt(index) {
  player.playVideoAt(index);
}

function setLoop(loop) {
  player.setLoop(loop);
}

function setShuffle(shuffle) {
  player.setShuffle(shuffle);
}
  </script>
</html>
        """
    }
}