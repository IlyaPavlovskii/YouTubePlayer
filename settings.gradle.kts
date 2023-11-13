enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo.repsy.io/mvn/chrynan/public")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo.repsy.io/mvn/chrynan/public")
    }
}

rootProject.name = "YouTubePlayer"
includeBuild("build-logic")

include(":androidApp")
include(":sample")
include(":features:youtubeplayer")
