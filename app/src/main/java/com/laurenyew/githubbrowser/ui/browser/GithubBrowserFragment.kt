package com.laurenyew.githubbrowser.ui.browser

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.laurenyew.githubbrowser.R
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepository
import com.laurenyew.githubbrowser.ui.ViewModelFactory
import com.laurenyew.githubbrowser.ui.browser.views.GithubBrowserRecyclerViewAdapter
import kotlinx.android.synthetic.main.github_browser_fragment.*
import javax.inject.Inject


class GithubBrowserFragment
@Inject constructor(private val viewModelFactory: ViewModelFactory) :
    Fragment() {
    private var adapter: GithubBrowserRecyclerViewAdapter? = null
    private lateinit var viewModel: GithubBrowserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.github_browser_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGithubBrowserListView()

        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[GithubBrowserViewModel::class.java]
        setupViewModelObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.github_browser_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == R.id.menu_refresh) {
            //TODO Refresh
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.onDestroy()
    }

    private fun setupGithubBrowserListView() {
        github_brower_recycler_view.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            //Add separator lines between rows
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun setupViewModelObservers() {
        // Setup view model observers
        viewModel.githubRepos.observe(viewLifecycleOwner, Observer { repos ->
            loadGithubRepoResults(repos)
        })
        viewModel.errorState.observe(viewLifecycleOwner, Observer { errorState ->
            updateErrorState(errorState)
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            updateLoadGithubRepoProgress(isLoading)
        })
    }

    private fun loadGithubRepoResults(results: List<GithubRepository>) {
        if (adapter == null) {
            adapter = GithubBrowserRecyclerViewAdapter()
            github_brower_recycler_view.adapter = adapter
        }
        adapter?.updateData(results)
        empty_github_browser.visibility =
            if (results.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateLoadGithubRepoProgress(isLoading: Boolean) {
        progress_bar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            empty_github_browser.visibility = View.GONE
        }
    }

    /**
     * Show error message for errors
     */
    private fun updateErrorState(errorState: ErrorState?) {
        error_state_message.visibility =
            if (errorState == ErrorState.NetworkError) View.VISIBLE else View.GONE
    }
}