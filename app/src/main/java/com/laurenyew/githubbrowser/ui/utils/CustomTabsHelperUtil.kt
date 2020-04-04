package com.laurenyew.githubbrowser.ui.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.laurenyew.githubbrowser.R


object CustomTabsHelperUtil {
    private const val SERVICE_ACTION =
        "android.support.customtabs.action.CustomTabsService"
    private const val CHROME_PACKAGE = "com.android.chrome"

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
}