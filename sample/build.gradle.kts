plugins {
    id("multiplatform-ui-convention")
}

android {
    namespace = "io.github.ilyapavlovskii.multiplatform.youtubeplayer.android.sample"
}

dependencies {
    //implementation(project(":features:youtubeplayer"))
    commonMainImplementation(projects.features.youtubeplayer)
}