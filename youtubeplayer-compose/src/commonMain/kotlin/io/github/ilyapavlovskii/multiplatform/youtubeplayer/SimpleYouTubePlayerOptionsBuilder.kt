package io.github.ilyapavlovskii.multiplatform.youtubeplayer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SimpleYouTubePlayerOptionsBuilder : YouTubePlayerOptionsBuilder {

    override var autoplay: Boolean = false
    override var controls: Boolean = true
    override var ccLoadPolicy: Boolean = true
    override var fullscreen: Boolean = false
    override var ivLoadPolicy: Boolean = false
    override var jsApiEnabled: Boolean = true
    override var mute: Boolean = false
    override var rel: Boolean = false
    override var langPref: String? = null
    override var list: String? = null
    override var listType: ListType? = null
    override var playsinline: Boolean = true

    override fun build(): String = json.encodeToString<Params>(createParams())

    private fun createParams(): Params = Params(
        autoplay = if (this.autoplay) 1 else 0,
        controls = if (this.controls) 1 else 0,
        ccLoadPolicy = if (this.ccLoadPolicy) 1 else 0,
        fullscreen = if (this.fullscreen) 1 else 0,
        ivLoadPolicy = if (this.ivLoadPolicy) 1 else 0,
        rel = if (this.rel) 3 else 1,
        langPref = this.langPref,
        list = this.list,
        listType = this.listType?.type,
        jsApi = if(this.jsApiEnabled) 1 else 0,
        mute = if(this.mute) 1 else 0,
        playsinline = if(this.playsinline) 1 else 0,
    )

    companion object {
        private const val AUTO_PLAY = "autoplay"
        private const val CONTROLS = "controls"
        private const val ENABLE_JS_API = "enablejsapi"
        private const val FS = "fs"
        private const val REL = "rel"
        private const val IV_LOAD_POLICY = "iv_load_policy"
        private const val CC_LOAD_POLICY = "cc_load_policy"
        private const val CC_LANG_PREF = "cc_lang_pref"
        private const val LIST = "list"
        private const val LIST_TYPE = "listType"
        private const val MUTE = "mute"
        private const val PLAYS_INLINE = "playsinline"
        private const val ORIGIN = "origin"
        private const val SHOW_INFO = "showinfo"
        private const val MODEST_BRANDING = "modestbranding"

        private val json by lazy { Json { encodeDefaults = true } }

        @Serializable
        data class Params(
            @SerialName(AUTO_PLAY)
            val autoplay: Int = 0,
            @SerialName(CONTROLS)
            val controls: Int = 1,
            @SerialName(CC_LOAD_POLICY)
            val ccLoadPolicy: Int = 1,
            @SerialName(FS)
            val fullscreen: Int = 0,
            @SerialName(IV_LOAD_POLICY)
            val ivLoadPolicy: Int = 0,
            @SerialName(REL)
            val rel: Int = 1,
            @SerialName(ENABLE_JS_API)
            val jsApi: Int = 1,
            @SerialName(MUTE)
            val mute: Int = 0,
            @SerialName(PLAYS_INLINE)
            val playsinline: Int = 1,

            @SerialName(ORIGIN)
            val origin: String = "https://www.youtube.com",
            @SerialName(SHOW_INFO)
            val showInfo: Int = 0,
            @SerialName(MODEST_BRANDING)
            val modestBranding: Int = 1,

            @SerialName(CC_LANG_PREF)
            val langPref: String? = null,
            @SerialName(LIST)
            val list: String? = null,
            @SerialName(LIST_TYPE)
            val listType: String? = null,
        )

        fun builder(
            init: YouTubePlayerOptionsBuilder.() -> Unit
        ): YouTubePlayerOptionsBuilder = SimpleYouTubePlayerOptionsBuilder().apply(init)
    }
}