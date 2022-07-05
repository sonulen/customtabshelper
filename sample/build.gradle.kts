plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 23
        targetSdk = 31

        versionName = "1.0"
        versionCode = 1
    }
}

dependencies {
    implementation(project(":customtabshelper"))
    implementation(kotlin("stdlib"))
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}

repositories {
    mavenCentral()
    google()
}
