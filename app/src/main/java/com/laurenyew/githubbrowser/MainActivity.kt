package com.laurenyew.githubbrowser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.laurenyew.githubbrowser.ui.browser.GithubBrowserFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    private lateinit var fragment: GithubBrowserFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }
}