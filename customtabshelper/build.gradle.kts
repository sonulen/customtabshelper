plugins {
    id(libs.plugins.android.library)
    id(kotlinx.plugins.kotlin.android.asProvider())
    id(kotlinx.plugins.kotlin.android.extensions)
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
    api(kotlin("stdlib"))
    api(androidx.browser)

    implementation(androidx.appcompat)
    implementation(androidx.material)
    implementation(androidx.ktx.viewbinding)
}
