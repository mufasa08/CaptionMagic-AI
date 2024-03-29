buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.13.3")

        classpath(Libraries.kotlin)
        classpath(Libraries.kotlinSerialization)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt") version ("1.22.0")

    kotlin("multiplatform") version "1.8.0" apply false
    kotlin("native.cocoapods") version "1.8.0"
}
