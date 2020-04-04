package com.laurenyew.githubbrowser

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.laurenyew.githubbrowser.ui.GithubBrowserActivity
import com.laurenyew.githubbrowser.ui.browser.GithubBrowserScreen
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GithubBrowserWalkthroughTest {
    private val browser = GithubBrowserScreen()

    @get:Rule
    var activityScenarioRule = activityScenarioRule<GithubBrowserActivity>()

    @Test
    fun happyPathWalkthroughSearchListDetail() {
        // Check initial status
        browser.searchTitleTextView.check(matches(isDisplayed()))
        browser.searchView.check(matches(isDisplayed()))
        browser.emptyTextView.check(matches(not(isDisplayed())))
        browser.errorTextView.check(matches(not(isDisplayed())))

        // Search
        browser.searchView.perform(typeText("google\n"))

        // Verify get results
        browser.repoRecyclerView.check(RecyclerViewItemCountAssertion(3))
        browser.emptyTextView.check(matches(not(isDisplayed())))
        browser.errorTextView.check(matches(not(isDisplayed())))

        // Click detail
        onView(withText("1")).perform(click())

        // Go back
        Espresso.pressBack()

        // Verify back on list
        browser.searchTitleTextView.check(matches(isDisplayed()))
        browser.searchView.check(matches(isDisplayed()))
        browser.repoRecyclerView.check(RecyclerViewItemCountAssertion(3))
        browser.emptyTextView.check(matches(not(isDisplayed())))
        browser.errorTextView.check(matches(not(isDisplayed())))
    }

    @Test
    fun errorPathWalkthroughSearchListShowsError() {
        // Search
        browser.searchView.perform(typeText("asdf\n"))

        // Verify get results
        browser.repoRecyclerView.check(RecyclerViewItemCountAssertion(0))
        browser.emptyTextView.check(matches(isDisplayed()))
        browser.errorTextView.check(matches(isDisplayed()))
    }

    class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {
        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            assertEquals(adapter!!.itemCount, expectedCount)
        }
    }
}
