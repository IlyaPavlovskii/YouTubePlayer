package io.github.ilyapavlovskii.multiplatform.youtubeplayer

interface YouTubePlayerOptionsBuilder {

    var autoplay: Boolean
    var controls: Boolean
    var ccLoadPolicy: Boolean
    var fullscreen: Boolean
    var ivLoadPolicy: Boolean
    var jsApiEnabled: Boolean
    var mute: Boolean
    var rel: Boolean
    var langPref: String?
    var list: String?
    var listType: ListType?

    /**
     * This parameter specifies whether the initial video will automatically start to play when
     * the player loads. The default value is false.
     *
     * If you enable Autoplay, playback will occur without any user interaction with the player;
     * playback data collection and sharing will therefore occur upon page load.
     * */
    fun autoplay(autoplay: Boolean): YouTubePlayerOptionsBuilder {
        this.autoplay = autoplay
        return this
    }

    /**
     * 	This parameter indicates whether the video player controls are displayed:
     * * False – Player controls do not display in the player.
     * * True – Player controls display in the player.
     * */
    fun controls(visible: Boolean): YouTubePlayerOptionsBuilder {
        this.controls = visible
        return this
    }

    /**
     * 	Setting the parameter's value to true causes closed captions to be shown by default,
     * 	even if the user has turned captions off. The default behavior is based on user preference.
     * */
    fun ccLoadPolicy(showCaptions: Boolean): YouTubePlayerOptionsBuilder {
        this.ccLoadPolicy = showCaptions
        return this
    }

    /**
     * Setting this parameter to false prevents the fullscreen button from displaying in the player.
     * The default value is true, which causes the fullscreen button to display.
     * */
    fun fullscreen(showFullscreenButton: Boolean): YouTubePlayerOptionsBuilder {
        this.fullscreen = showFullscreenButton
        return this
    }

    /**
     * Setting the parameter's value to true causes video annotations to be shown by default,
     * whereas setting to false causes video annotations to not be shown by default.
     *
     * The default value is true.
     * */
    fun ivLoadPolicy(showVideoAnnotations: Boolean): YouTubePlayerOptionsBuilder {
        this.ivLoadPolicy = showVideoAnnotations
        return this
    }

    /**
     * Prior to the change, this parameter indicates whether the player should show related
     * videos when playback of the initial video ends.
     *
     * * If the parameter's value is set to true, which is the default value,
     * then the player does show related videos.
     * * If the parameter's value is set to false, then the player does not show related videos.
     *
     * After the change, you will not be able to disable related videos.
     * Instead, if the rel parameter is set to false, related videos will come from the same channel as the video that was just played.
     * */
    fun rel(showRelatedVideos: Boolean): YouTubePlayerOptionsBuilder {
        this.rel = showRelatedVideos
        return this
    }

    /**
     * 	This parameter specifies the default language that the player will use to display captions.
     * 	Set the parameter's value to an ISO 639-1 two-letter language code.
     *
     * If you use this parameter and also set the cc_load_policy parameter to true,
     * then the player will show captions in the specified language when the player loads.
     * If you do not also set the cc_load_policy parameter,
     * then captions will not display by default, but will display in the specified
     * language if the user opts to turn captions on.
     * */
    fun langPref(languageCode: String): YouTubePlayerOptionsBuilder {
        this.langPref = languageCode
        return this
    }

    /**
     * 	The list parameter, in conjunction with the listType parameter, identifies the content that will load in the player.
     * If the listType parameter value is user_uploads, then the list parameter value identifies the YouTube channel whose uploaded videos will be loaded.
     * If the listType parameter value is playlist, then the list parameter value specifies a YouTube playlist ID. In the parameter value, you need to prepend the playlist ID with the letters PL as shown in the example below.
     *
     * https://www.youtube.com/embed?
     *     listType=playlist
     *     &list=PLC77007E23FF423C6
     * If the listType parameter value is search, then the list parameter value specifies the search query. Note: This functionality is deprecated and will no longer be supported as of 15 November 2020.
     * Note: If you specify values for the list and listType parameters, the IFrame embed URL does not need to specify a video ID.
     * */
    fun list(list: String): YouTubePlayerOptionsBuilder {
        this.list = list
        return this
    }

    /**
     * 	The listType parameter, in conjunction with the list parameter, identifies the content
     * 	that will load in the player. Valid parameter values are playlist and user_uploads.
     *
     * If you specify values for the list and listType parameters, the IFrame embed
     * URL does not need to specify a video ID.
     * */
    fun listType(listType: ListType): YouTubePlayerOptionsBuilder {
        this.listType = listType
        return this
    }

    /**
     * 	Setting the parameter's value to true enables the player to be controlled via IFrame Player
     * 	API calls. The default value is false, which means that the player cannot be controlled
     * 	using that API.
     *
     * For more information on the IFrame API and how to use it, see the IFrame API documentation.
     * */
    fun enableJsApi(jsApiEnabled: Boolean): YouTubePlayerOptionsBuilder {
        this.jsApiEnabled = jsApiEnabled
        return this
    }

    /**
     * Mute or unmute video
     * */
    fun mute(mute: Boolean): YouTubePlayerOptionsBuilder {
        this.mute = mute
        return this
    }

    fun build(): String
}

enum class ListType(val type: String) {
    USER_UPLOADS("user_uploads"),
    PLAYLIST("playlist"),
    ;
}
