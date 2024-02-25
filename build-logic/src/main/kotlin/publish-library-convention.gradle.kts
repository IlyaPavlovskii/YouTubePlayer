@file:Suppress("UnstableApiUsage")

import com.vanniktech.maven.publish.SonatypeHost


plugins {
    id("com.vanniktech.maven.publish")
}

val versionSuffix = when (System.getenv("RELEASE")) {
    "true" -> ""
    else -> "-SNAPSHOT"
}
project.version = project.version.toString() + versionSuffix

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01)

    coordinates(
        groupId = project.group.toString(),
        artifactId = project.name,
        version = project.version.toString()
    )

    pom {
        name.set("YouTubePlayer")
        description.set("The `YouTubePlayer` composable allows you to embed a YouTube video player in your Jetpack Compose app.")
        inceptionYear.set("2024")
        url.set("https://github.com/IlyaPavlovskii/YouTubePlayer")

        licenses {
            license {
                name.set("Apache 2.0 License")
                url.set("https://github.com/IlyaPavlovskii/YouTubePlayer/blob/master/LICENSE.md")
                distribution.set("https://github.com/IlyaPavlovskii/YouTubePlayer/blob/master/LICENSE.md")
            }
        }
        developers {
            developer {
                id.set("Ilia Pavlovskii")
                name.set("Ilia Pavlovskii")
                url.set("pavlovskii.ilya@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/IlyaPavlovskii/YouTubePlayer")
            connection.set("scm:git:github.com/IlyaPavlovskii/YouTubePlayer.git")
            developerConnection.set("scm:git:ssh://github.com/IlyaPavlovskii/YouTubePlayer.git")
        }
    }
    // https://github.com/vanniktech/gradle-maven-publish-plugin/blob/46c85b2a306d3a3cd675f26d4b4176ad3ea477a1/docs/central.md
    signAllPublications()

}
