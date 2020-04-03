package com.laurenyew.githubbrowser.ui.browser

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.laurenyew.githubbrowser.R
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryModel
import com.laurenyew.githubbrowser.ui.ViewModelFactory
import com.laurenyew.githubbrowser.ui.browser.views.GithubBrowserRecyclerViewAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.github_browser_fragment.*
import javax.inject.Inject


class GithubBrowserFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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
        setupSearchView()
        setupGithubBrowserListView()

        viewModel =
            ViewModelProvider(this, viewModelFactory)[GithubBrowserViewModel::class.java]
        setupViewModelObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.github_browser_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == R.id.menu_refresh) {
            searchGithubRepos()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.onDestroy()
    }

    private fun setupSearchView() {
        github_browser_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchGithubRepos()
                val imm =
                    activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view?.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
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

    private fun searchGithubRepos() {
        val organizationName = github_browser_search_view.query.toString()
        viewModel.searchGithubForTopReposBy(organizationName)
    }

    private fun loadGithubRepoResults(results: List<GithubRepositoryModel>) {
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

    companion object {
        fun newInstance() = GithubBrowserFragment()
    }
}