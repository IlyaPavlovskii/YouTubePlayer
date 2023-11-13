package io.github.ilyapavlovskii.multiplatform.youtubeplayer

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}