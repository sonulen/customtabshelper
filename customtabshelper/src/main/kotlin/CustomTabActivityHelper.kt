package com.redmadrobot.customtabshelper

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import com.redmadrobot.customtabshelper.internal.CustomTabsHelper.getPackageNameToUse
import com.redmadrobot.customtabshelper.internal.ServiceConnection
import com.redmadrobot.customtabshelper.internal.ServiceConnectionCallback
import com.redmadrobot.customtabshelper.ui.WebViewActivity

/**
 * This is a helper class to manage the connection to the Custom Tabs Service.
 *
 * Source copied from Google's sample and converted to Kotlin:
 * https://github.com/GoogleChrome/android-browser-helper/blob/acf09c71697151088f96878a25f284fc3734fff8/demos/custom-tabs-example-app/src/main/java/org/chromium/customtabsdemos/internal.CustomTabActivityHelper.java
 */
public class CustomTabActivityHelper : ServiceConnectionCallback {

    /** Creates or retrieves an exiting CustomTabsSession. */
    public val session: CustomTabsSession?
        get() {
            if (customTabsSession == null) customTabsSession = client?.newSession(null)
            return customTabsSession
        }

    private var customTabsSession: CustomTabsSession? = null
    private var client: CustomTabsClient? = null
    private var connection: CustomTabsServiceConnection? = null
    private var connectionCallback: ConnectionCallback? = null

    /**
     * Opens the [uri] on a Custom Tab if possible. Otherwise [fallback] to opening it on a WebView.
     *
     * @param activity         The host activity.
     * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available.
     */
    @Suppress("UNUSED_PARAMETER")
    public fun openCustomTab(
        activity: Activity,
        customTabsIntent: CustomTabsIntent,
        uri: Uri,
        fallback: CustomTabFallback = WEBVIEW_FALLBACK,
    ) {
        val packageName = getPackageNameToUse(activity)

        // If we can't find a package name, it means there's no browser that supports
        // Chrome Custom Tabs installed. So, we fallback to the WebView
        if (packageName == null) {
            fallback.openUri(activity, uri)
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(activity, uri)
        }
    }

    /**
     * Unbinds the Activity from the Custom Tabs Service.
     * @param activity the activity that is connected to the service.
     */
    public fun unbindCustomTabsService(activity: Activity) {
        if (connection == null) return
        connection?.let(activity::unbindService)
        client = null
        customTabsSession = null
        connection = null
    }

    /** Register a Callback to be called when connected or disconnected from the Custom Tabs Service. */
    public fun setConnectionCallback(connectionCallback: ConnectionCallback) {
        this.connectionCallback = connectionCallback
    }

    /** Binds the [activity] to the Custom Tabs Service. */
    public fun bindCustomTabsService(activity: Activity) {
        if (client != null) return
        val packageName = getPackageNameToUse(activity) ?: return
        connection = ServiceConnection(this).also {
            CustomTabsClient.bindCustomTabsService(activity, packageName, it)
        }
    }

    /**
     * Returns `true` if call to mayLaunchUrl was accepted.
     * @see CustomTabsSession.mayLaunchUrl
     */
    public fun mayLaunchUrl(uri: Uri, extras: Bundle?, otherLikelyBundles: List<Bundle>?): Boolean {
        if (client == null) return false
        return session?.mayLaunchUrl(uri, extras, otherLikelyBundles) == true
    }

    override fun onServiceConnected(client: CustomTabsClient) {
        this.client = client
        client.warmup(0)
        connectionCallback?.onCustomTabsConnected()
    }

    override fun onServiceDisconnected() {
        client = null
        customTabsSession = null
        connectionCallback?.onCustomTabsDisconnected()
    }

    /**
     * A Callback for when the service is connected or disconnected. Use those callbacks to
     * handle UI changes when the service is connected or disconnected.
     */
    public interface ConnectionCallback {
        /** Called when the service is connected. */
        public fun onCustomTabsConnected()

        /** Called when the service is disconnected. */
        public fun onCustomTabsDisconnected()
    }

    /**
     * To be used as a fallback to open the Uri when Custom Tabs is not available.
     */
    public fun interface CustomTabFallback {
        /**
         * @param activity The Activity that wants to open the Uri.
         * @param uri      The uri to be opened by the fallback.
         */
        public fun openUri(activity: Activity, uri: Uri)
    }

    private companion object {
        val WEBVIEW_FALLBACK = CustomTabFallback { activity, uri ->
            val intent = WebViewActivity.getIntent(activity, uri)
            activity.startActivity(intent)
        }
    }
}
