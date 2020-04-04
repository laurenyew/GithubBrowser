package com.laurenyew.githubbrowser.ui.browser

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.laurenyew.githubbrowser.R

class GithubBrowserScreen {
    val searchTitleTextView = onView(withId(R.id.search_title))
    val searchView = onView(withId(R.id.github_browser_search_view))
    val swipeRefreshView = onView(withId(R.id.browser_swipe_refresh))
    val repoRecyclerView = onView(withId(R.id.github_brower_recycler_view))
    val emptyTextView = onView(withId(R.id.empty_github_browser))
    val errorTextView = onView(withId(R.id.error_state_message))
}