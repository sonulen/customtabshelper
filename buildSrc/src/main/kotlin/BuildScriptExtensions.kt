@file:Suppress("UnstableApiUsage")

import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependency
import org.gradle.plugin.use.PluginDependencySpec

fun PluginDependenciesSpec.id(pluginDependency: Provider<PluginDependency>): PluginDependencySpec {
    return id(pluginDependency.id)
}

val Provider<PluginDependency>.id: String get() = get().pluginId
