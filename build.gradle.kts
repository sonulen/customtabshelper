plugins {
    alias(libs.plugins.versions)
    alias(libs.plugins.cache.fix) apply false
}

buildscript {
    //  FIXME Почему мне пришлось добавлять их в classpath
    dependencies {
        classpath(libs.gradle)
        classpath(kotlinx.gradle)
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply(plugin = "detekt-convention")

    plugins.withType<com.android.build.gradle.api.AndroidBasePlugin> {
        apply(plugin = rootProject.libs.plugins.cache.fix.get().pluginId)
    }
}
