plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.kotlinCompose).apply(false)
    alias(libs.plugins.vanniktech.maven.publish).apply(false)
}

subprojects {
    group = "io.github.ilyapavlovskii"
    version = "2026.01.27"
}