plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.redmadrobot.customtabshelper"
        minSdk = 23
        targetSdk = 31

        versionName = "1.0"
        versionCode = 1
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":customtabshelper"))
    implementation(kotlin("stdlib"))
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
