// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        mavenCentral()
        
    }
    dependencies {
        classpath (Dependencies.Gradle.gradle)
        classpath (Dependencies.Kotlin.gradlePlugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        mavenCentral()
    }
}

tasks.register("clean",Delete::class) {
    delete(rootProject.buildDir)
}