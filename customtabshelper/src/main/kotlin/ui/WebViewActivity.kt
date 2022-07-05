package com.redmadrobot.customtabshelper.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.redmadrobot.customtabshelper.databinding.ActivityWebviewBinding
import com.redmadrobot.extensions.viewbinding.inflateViewBinding

/**
 * This Activity is used as a fallback when there is no browser installed that supports
 * Chrome Custom Tabs
 */
internal class WebViewActivity : AppCompatActivity() {

    private val webViewUrl: String by lazy {
        requireNotNull(intent.getStringExtra(EXTRA_URL)) { "$EXTRA_URL is not defined." }
    }

    @SuppressLint("SetJavaScriptEnabled") // Ok because it is fallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = inflateViewBinding<ActivityWebviewBinding>()
        setContentView(binding.root)

        with(binding.webview) {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    return shouldOverrideUrlLoading(request.url)
                }
            }

            settings.javaScriptEnabled = true
            loadUrl(webViewUrl)
        }

        title = webViewUrl
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Respond to the action bar's Up/Home button
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun shouldOverrideUrlLoading(navigationUrl: Uri): Boolean {
        // URIs with the `data` scheme are handled in the WebView.
        // The "Demo" item in https://jakearchibald.github.io/svgomg/ is one example of this
        // usage
        if ("data" == navigationUrl.scheme) return false

        // Если можем обработать ссылку нашим приложением, обрабатываем, если нет - оставляем её для WebView
        return try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = navigationUrl
                setPackage(application.packageName)
                addFlags(FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            Log.e("custom-tabs-helper", "Error while launching '$navigationUrl'", e)
            false
        }
    }

    companion object {
        private const val EXTRA_URL = "extra.url"

        fun getIntent(activity: Activity, uri: Uri): Intent {
            return Intent(activity, WebViewActivity::class.java).apply {
                putExtra(EXTRA_URL, uri.toString())
            }
        }
    }
}
