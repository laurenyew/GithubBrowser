package com.laurenyew.githubbrowser.ui.detail

import android.os.Bundle
import com.laurenyew.githubbrowser.R
import dagger.android.DaggerActivity
import kotlinx.android.synthetic.main.github_repo_detail_activity.*

/**
 * Simple WebView activity to show the given Github Repository webpage
 * (This is a fallback activity if Chrome Tabs is not available on the phone)
 */
class GithubRepoDetailActivity : DaggerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_repo_detail_activity)
        if (savedInstanceState == null) {
            intent.extras?.getString(WEBSITE_URL_KEY)?.let { url ->
                web_container.loadUrl(url)
            }
        }
    }

    companion object {
        const val WEBSITE_URL_KEY = "website_url"
    }
}