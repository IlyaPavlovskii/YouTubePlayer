plugins {
    id("multiplatform-ui-convention")
}

kotlin {
    androidLibrary {
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