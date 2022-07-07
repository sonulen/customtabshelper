pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "Custom tabs helper"

include(
    ":sample",
    ":customtabshelper"
)
