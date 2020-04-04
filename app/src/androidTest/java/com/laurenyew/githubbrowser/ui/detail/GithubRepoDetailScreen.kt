package com.laurenyew.githubbrowser.ui.detail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.laurenyew.githubbrowser.R

class GithubRepoDetailScreen {
    val webview = onView(withId(R.id.web_container))
}