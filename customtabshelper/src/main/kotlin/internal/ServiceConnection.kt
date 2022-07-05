package com.redmadrobot.customtabshelper.internal

import android.content.ComponentName
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection
import java.lang.ref.WeakReference

/**
 * Implementation for the CustomTabsServiceConnection that avoids leaking the
 * internal.ServiceConnectionCallback
 */
internal class ServiceConnection(connectionCallback: ServiceConnectionCallback) : CustomTabsServiceConnection() {

    // A weak reference to the internal.ServiceConnectionCallback to avoid leaking it.
    private val connectionCallback = WeakReference(connectionCallback)

    override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
        val connectionCallback = connectionCallback.get()
        connectionCallback?.onServiceConnected(client)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        val connectionCallback = connectionCallback.get()
        connectionCallback?.onServiceDisconnected()
    }
}
