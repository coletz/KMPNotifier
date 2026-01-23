import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinNativeCocoaPods)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    explicitApi()
    androidTarget {
        publishAllLibraryVariants()
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    js(IR) {
        nodejs()
        browser()
        binaries.library()
    }

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()


    cocoapods {
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "KMPNotifier"
            isStatic = true
        }
        noPodspec()
        pod("FirebaseMessaging")
    }



    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.startup.runtime)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.ktx)
            implementation(libs.firebase.messaging)

        }
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutine)
        }
        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

    }
}

android {
    namespace = "com.mmk.kmpnotifier"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.mirzemehdi.clzfork",
        artifactId = "kmpnotifier",
        version = project.properties["kmpNotifierVersion"] as String
    )
    pom {
        name = "KMPNotifier"
        description = "Kotlin Multiplatform Push Notification Library targeting ios and android"
        url = "https://github.com/EggonDev/KMPNotifier"

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
            connection.set("https://github.com/EggonDev/KMPNotifier.git")
            url.set("https://github.com/EggonDev/KMPNotifier")
        }
    }
}

