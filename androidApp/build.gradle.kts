plugins {
    id("android-app-convention")
    alias(libs.plugins.kotlinCompose)
}

android {
    namespace = "io.github.ilyapavlovskii.multiplatform.youtubeplayer.android"
    defaultConfig {
        applicationId = "io.github.ilyapavlovskii.multiplatform.youtubeplayer.android"
    }
}

dependencies {
    implementation(projects.sample)
}
