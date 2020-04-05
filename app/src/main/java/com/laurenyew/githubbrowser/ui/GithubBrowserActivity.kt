package com.laurenyew.githubbrowser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.laurenyew.githubbrowser.R
import com.laurenyew.githubbrowser.ui.browser.GithubBrowserFragment

/**
 * Wrapper activity for Github Browser feature
 */
class GithubBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_browser_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GithubBrowserFragment.newInstance())
                .commitNow()
        }
    }
}