pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("androidx") { from(files("gradle/androidx.versions.toml")) }
        create("kotlinx") { from(files("gradle/kotlinx.versions.toml")) }
    }
}

rootProject.name = "Custom tabs helper"

include(
    ":sample",
    ":customtabshelper"
)
