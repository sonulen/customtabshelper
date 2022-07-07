plugins {
    id("com.github.ben-manes.versions") version "0.41.0"
    id("org.gradle.android.cache-fix") version "2.5.5" apply false
    id("com.redmadrobot.detekt") version "0.16.2"
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

subprojects {
    repositories {
        google()
        mavenLocal()
    }

    apply(plugin = "com.redmadrobot.detekt")

    plugins.withType<com.android.build.gradle.api.AndroidBasePlugin>() {
        apply(plugin = "org.gradle.android.cache-fix")
    }
}
