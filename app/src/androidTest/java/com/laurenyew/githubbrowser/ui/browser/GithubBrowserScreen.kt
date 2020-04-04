package com.laurenyew.githubbrowser.ui.browser

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.laurenyew.githubbrowser.R

/**
 * Helper class to hold all the espresso hooks for Github Browser
 */
class GithubBrowserScreen {
    val searchTitleTextView: ViewInteraction = onView(withId(R.id.search_title))
    val searchView: ViewInteraction = onView(withId(R.id.github_browser_search_view))
    val repoRecyclerView: ViewInteraction = onView(withId(R.id.github_brower_recycler_view))
    val emptyTextView: ViewInteraction = onView(withId(R.id.empty_github_browser))
    val errorTextView: ViewInteraction = onView(withId(R.id.error_state_message))
}