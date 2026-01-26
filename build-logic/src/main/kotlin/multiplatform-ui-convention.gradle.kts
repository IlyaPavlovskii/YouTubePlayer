import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

val libs = the<LibrariesForLibs>()

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = project.name
            isStatic = true
        }
        iosTarget.binaries.all {
            freeCompilerArgs += listOf("-Xbinary=targetIosVersion=17.2")
        }
    }

    cocoapods {
        // Required properties
        // Specify the required Pod version here. Otherwise, the Gradle project version is used.
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"

        ios.deploymentTarget = "17.2"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
        // name = "MyCocoaPod"

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = project.name

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            // isStatic = false
            // Dependency export
            //            export(project(":anotherKMMModule"))
            //            transitiveExport = false // This is default.
            // Bitcode embedding
            //embedBitcode(BitcodeEmbeddingMode.BITCODE)
        }

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val androidMain by getting {
            dependencies {
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
