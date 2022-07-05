package com.redmadrobot.customtabshelper.internal

import androidx.browser.customtabs.CustomTabsClient

/** Callback for events when connecting and disconnecting from Custom Tabs Service. */
internal interface ServiceConnectionCallback {

    /**
     * Called when the service is connected.
     * @param client a CustomTabsClient
     */
    fun onServiceConnected(client: CustomTabsClient)

    /** Called when the service is disconnected. */
    fun onServiceDisconnected()
}
