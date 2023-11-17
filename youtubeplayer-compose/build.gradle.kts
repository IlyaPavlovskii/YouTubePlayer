plugins {
    id("multiplatform-ui-convention")
    id("publish-library-convention")
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "io.github.ilyapavlovskii.multiplatform.youtubeplayer"
}

dependencies {
    commonMainImplementation(libs.io.github.kevinnzou.webview)
    commonMainImplementation(libs.org.jetbrains.kotlinx.serialization.json)
    commonMainImplementation(libs.org.jetbrains.kotlinx.datetime)
    commonMainImplementation(libs.com.chrynan.uri.core)
}