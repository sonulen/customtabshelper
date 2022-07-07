package com.redmadrobot.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.COLOR_SCHEME_LIGHT
import androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_OFF
import com.redmadrobot.customtabshelper.CustomTabActivityHelper
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Sample activity
 */
class MainActivity : AppCompatActivity() {

    private val customTabHelper by lazy { CustomTabActivityHelper() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { openUrl() }
    }

    override fun onStart() {
        super.onStart()
        customTabHelper.bindCustomTabsService(this)
    }

    override fun onStop() {
        customTabHelper.unbindCustomTabsService(this)
        super.onStop()
    }

    @SuppressLint("SetTextI18n")
    override fun onNewIntent(intent: Intent) {
        val data = intent.data ?: return
        val callback = data.toString()
        textView.text = "Callback with url $callback"

        super.onNewIntent(intent)
    }

    private fun openUrl(url: String = "https://github.com/sonulen/customtabshelper") {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setColorScheme(COLOR_SCHEME_LIGHT)
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(applicationContext.getColor(R.color.colorSurface))
                    .build()
            )
            .setShareState(SHARE_STATE_OFF)
            .apply { customTabHelper.session?.let(::setSession) }
            .build()

        customTabHelper.openCustomTab(this, customTabsIntent, Uri.parse(url))
    }
}
