plugins {
    id("android-app-convention")
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
