package com.redmadrobot.customtabshelper.internal

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsService

/**
 * Helper class for Custom Tabs.
 *
 * Source copied from Google's sample and converted to Kotlin:
 * https://github.com/GoogleChrome/android-browser-helper/blob/acf09c71697151088f96878a25f284fc3734fff8/demos/custom-tabs-example-app/src/main/java/org/chromium/customtabsdemos/internal.CustomTabsHelper.java
 */
internal object CustomTabsHelper {

    const val STABLE_PACKAGE = "com.android.chrome"
    const val BETA_PACKAGE = "com.chrome.beta"
    const val DEV_PACKAGE = "com.chrome.dev"
    const val LOCAL_PACKAGE = "com.google.android.apps.chrome"

    private var packageNameToUse: String? = null

    /**
     * Goes through all apps that handle VIEW intents and have a warmup service. Picks
     * the one chosen by the user if there is one, otherwise makes a best effort to return a
     * valid package name.
     *
     * This is **not** threadsafe.
     *
     * @param context to use for accessing [PackageManager].
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    fun getPackageNameToUse(context: Context): String? {
        if (packageNameToUse != null) return packageNameToUse

        val packageManager = context.packageManager
        // Get default VIEW intent handler.
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
        val defaultViewHandlerPackageName = packageManager.resolveActivity(activityIntent, 0)
            ?.activityInfo
            ?.packageName
            ?.takeUnless { it.isEmpty() }

        val packagesSupportingCustomTabs = packageManager.resolvePackagesSupportingCustomTabs(activityIntent)
        packageNameToUse = when {
            packagesSupportingCustomTabs.size < 2 -> packagesSupportingCustomTabs.firstOrNull()

            defaultViewHandlerPackageName != null &&
                !packageManager.hasSpecializedHandlerIntents(activityIntent) &&
                defaultViewHandlerPackageName in packagesSupportingCustomTabs -> defaultViewHandlerPackageName

            STABLE_PACKAGE in packagesSupportingCustomTabs -> STABLE_PACKAGE
            BETA_PACKAGE in packagesSupportingCustomTabs -> BETA_PACKAGE
            DEV_PACKAGE in packagesSupportingCustomTabs -> DEV_PACKAGE
            LOCAL_PACKAGE in packagesSupportingCustomTabs -> LOCAL_PACKAGE
            else -> null
        }

        return packageNameToUse
    }

    /** Returns all apps that can handle both given [activityIntent] and Custom Tabs service calls. */
    private fun PackageManager.resolvePackagesSupportingCustomTabs(activityIntent: Intent): List<String> {
        val resolvedActivityList = queryIntentActivities(activityIntent, 0)
        return resolvedActivityList.mapNotNull { info ->
            val serviceIntent = Intent().apply {
                action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
                setPackage(info.activityInfo.packageName)
            }

            if (resolveService(serviceIntent, 0) != null) {
                info.activityInfo.packageName
            } else {
                null
            }
        }
    }

    /**
     * Used to check whether there is a specialized handler for a given intent.
     * @param intent The intent to check with.
     * @return Whether there is a specialized handler for the given intent.
     */
    private fun PackageManager.hasSpecializedHandlerIntents(intent: Intent): Boolean {
        return try {
            queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER)
                .asSequence()
                .filter { it.activityInfo != null }
                .mapNotNull { it.filter }
                .any { it.countDataAuthorities() != 0 && it.countDataPaths() != 0 }
        } catch (ignored: RuntimeException) {
            Log.e("custom-tabs-helper", "Runtime exception while getting specialized handlers", ignored)
            false
        }
    }
}
