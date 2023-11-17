repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.toolsGradle)
    implementation(libs.composeGradlePlugin)
    implementation(libs.com.vanniktech.gradle.maven.publish.plugin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}
project.tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}