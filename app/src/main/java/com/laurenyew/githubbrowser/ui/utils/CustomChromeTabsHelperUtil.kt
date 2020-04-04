package com.laurenyew.githubbrowser.ui.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.browser.customtabs.*
import com.laurenyew.githubbrowser.R


object CustomChromeTabsHelperUtil {
    private const val SERVICE_ACTION =
        "android.support.customtabs.action.CustomTabsService"
    private const val CHROME_PACKAGE = "com.android.chrome"
    private var currentClient: CustomTabsClient? = null

    fun isChromeCustomTabsSupported(context: Context?): Boolean {
        val serviceIntent = Intent(SERVICE_ACTION)
        serviceIntent.setPackage(CHROME_PACKAGE)
        val resolveInfoList: List<ResolveInfo>? =
            context?.packageManager?.queryIntentServices(serviceIntent, 0)
        return !resolveInfoList.isNullOrEmpty()
    }

    fun openCustomChromeTab(context: Context?, websiteUrl: String) {
        context?.let {
            val customTabsIntent = CustomTabsIntent.Builder().apply {
                addDefaultShareMenuItem()
                setToolbarColor(context.resources.getColor(R.color.colorPrimary))
                setShowTitle(true)

            }.build()
            customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            customTabsIntent.launchUrl(it, Uri.parse(websiteUrl))
        }
    }

    /**
     * Make sure to warm up the Chrome Custom Tabs for URLs so they'll load faster
     */
    fun warmupChromeTabs(context: Context?, urls: List<String>?) {
        if (context != null && urls != null) {
            // Binds to the service.
            CustomTabsClient.bindCustomTabsService(
                context,
                CHROME_PACKAGE,
                object : CustomTabsServiceConnection() {
                    override fun onCustomTabsServiceConnected(
                        name: ComponentName,
                        client: CustomTabsClient
                    ) {
                        // client is now valid.
                        currentClient = client
                    }

                    override fun onServiceDisconnected(name: ComponentName) {
                        // client is no longer valid. This also invalidates sessions.
                        currentClient = null
                    }
                })

            currentClient?.warmup(0)

            val session: CustomTabsSession? = currentClient?.newSession(CustomTabsCallback())

            urls.forEach { url ->
                session?.mayLaunchUrl(Uri.parse(url), null, null)
            }
        }
    }

    fun clearChromeTabs(){
        currentClient = null
    }
}