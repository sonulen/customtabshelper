plugins {
//    id("com.osacky.doctor") version "0.8.1"
    id("com.redmadrobot.android-config") version "0.16"
    id("com.redmadrobot.detekt") version "0.16"
}

subprojects {
    apply(plugin = "com.redmadrobot.detekt")
}

redmadrobot {
    android {
        minSdk.set(21)
        targetSdk.set(31)
    }
}
