import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

val key: String = gradleLocalProperties(rootDir).getProperty("OPEN_API_SECRET")

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
    id("io.kotest.multiplatform") version "5.0.2"
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libraries.Common.Ktor.core)
                implementation(Libraries.Common.Ktor.content)
                implementation(Libraries.Common.Ktor.serializationJson)
                implementation(Libraries.Common.Ktor.logging)
                implementation("io.ktor:ktor-client-content-negotiation:2.2.1")
                implementation(Libraries.Common.sqlDelight)
                implementation(Libraries.Common.sqlDelightExtension)
                implementation(Libraries.Common.kotlinxSerializationCore)
                implementation(Libraries.Common.kotlinxCoroutinesCore)
                implementation(Libraries.Common.koinCore)

                // settings
                implementation("com.russhwolf:multiplatform-settings-no-arg:1.0.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("io.kotest:kotest-framework-engine:5.0.2")
                implementation("'io.kotest:kotest-assertions-core:5.0.2")
                implementation("io.kotest:kotest-property:5.0.2")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Libraries.Android.sqlDelight)
                implementation(Libraries.Android.ktorClient)

                implementation("com.google.firebase:firebase-analytics-ktx:21.2.0")
                implementation("com.google.firebase:firebase-crashlytics:18.3.2")
                implementation("com.google.firebase:firebase-perf:20.3.0")
                implementation("com.google.firebase:firebase-messaging:23.1.1")
                implementation("com.google.firebase:firebase-auth-ktx:21.1.0")

                // image detection
                implementation("com.google.mlkit:image-labeling:17.0.7")
                implementation("com.google.mlkit:image-labeling-custom:17.0.1")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
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
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.devinjapan.aisocialmediaposter.shared"
    compileSdk = Versions.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

buildkonfig {
    packageName = "com.devinjapan.aisocialmediaposter"
    // run this
    // ./gradlew generateBuildKonfig

    defaultConfigs {
        buildConfigField(STRING, "OpenApiSecret", key)
    }
}
