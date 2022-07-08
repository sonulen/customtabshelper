import java.util.*
import com.android.build.api.dsl.*

plugins {
    id(libs.plugins.android.application)
    id(kotlinx.plugins.kotlin.android)
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

    buildFeatures {
        buildConfig = false
        resValues = false
        viewBinding = true
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
        }

        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }

        signingConfigs {
            val debugStoreFile = rootProject.file("cert/debug.keystore")
            val debugPropsFile = rootProject.file("cert/debug.properties")
            val debugProps = Properties()
            debugPropsFile.inputStream().use(debugProps::load)

            val debugSigningConfig = getByName("debug") {
                storeFile = debugStoreFile
                keyAlias = debugProps.getProperty("key_alias")
                keyPassword = debugProps.getProperty("key_password")
                storePassword = debugProps.getProperty("store_password")
            }

            release {
                signingConfig = debugSigningConfig
            }
        }
    }
}

dependencies {
    implementation(project(":customtabshelper"))
    implementation(kotlin("stdlib"))

    implementation(androidx.appcompat)
    implementation(androidx.constraintlayout)
}
