plugins {
    id("com.redmadrobot.android-library")
}

android {
    buildFeatures {
        viewBinding = true
        androidResources = true
    }
}

dependencies {
    api("androidx.browser:browser:1.4.0")

    implementation(kotlin("stdlib"))
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.redmadrobot.extensions:viewbinding-ktx:4.2.1-0")
}
repositories {
    mavenCentral()
}
