plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xexplicit-api=strict"
    }

    buildFeatures {
        buildConfig = false
        resValues = false
        viewBinding = true
        // why?
        androidResources = true
    }
}

dependencies {
    api("androidx.browser:browser:1.4.0")
    api(kotlin("stdlib"))

    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.redmadrobot.extensions:viewbinding-ktx:4.2.1-0")
}
