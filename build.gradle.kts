import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinNativeCocoaPods) apply false
    alias(libs.plugins.kotlinx.binary.validator)
    alias(libs.plugins.kmp.maven.publish)
    id("com.google.gms.google-services") version "4.4.2" apply false
}




allprojects {
    group = "io.github.mirzemehdi.clzfork"
    version = project.properties["kmpNotifierVersion"] as String

    val excludedModules = listOf(":sample")
    if (project.path in excludedModules) return@allprojects

    apply(plugin = "maven-publish")

    extensions.configure<PublishingExtension> {
        repositories {
            maven {
                name = "githubPackages"
                url = uri("https://maven.pkg.github.com/traceonio/KMPNotifier")
                credentials(PasswordCredentials::class)
            }
        }
    }
}

mavenPublishing {
    // Define coordinates for the published artifact
    coordinates(
        groupId = "io.github.mirzemehdi.clzfork",
        artifactId = "kmpnotifier",
        version = project.properties["kmpNotifierVersion"] as String
    )

    pom {
        name.set("KMPNotifier")
        description.set(" Kotlin Multiplatform Push Notification Library targeting ios and android")
        url.set("https://github.com/traceonio/KMPNotifier")

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://opensource.org/licenses/Apache-2.0")
            }
        }

        developers {
            developer {
                name.set("Mirzamehdi Karimov")
                email.set("mirzemehdi@gmail.com")
            }
            developer {
                id.set("coletz")
                name.set("coletz")
                email.set("dcoletto.sw@gmail.com")
            }
        }

        scm {
            connection.set("https://github.com/traceonio/KMPNotifier.git")
            url.set("https://github.com/traceonio/KMPNotifier")
        }
    }
}