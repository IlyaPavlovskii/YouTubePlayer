package io.github.ilyapavlovskii.multiplatform.youtubeplayer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform