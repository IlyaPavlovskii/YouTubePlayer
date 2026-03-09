plugins {
    id("multiplatform-ui-convention")
}

kotlin {
    android {
        namespace = "io.github.ilyapavlovskii.multiplatform.youtubeplayer.android.sample"
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.ui.tooling.preview.android)
        }
        commonMain.dependencies {
            implementation(projects.youtubeplayerCompose)
        }
    }
}