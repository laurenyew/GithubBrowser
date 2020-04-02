package com.laurenyew.githubbrowser.ui.browser

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.laurenyew.githubbrowser.R
import kotlinx.android.synthetic.main.github_browser_fragment.*

class GithubBrowserFragment : Fragment() {
    //    private var adapter: GithubBrowserRecyclerViewAdapter? = null
    private val viewModel: GithubBrowserViewModel by viewModels()

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

        // TODO Setup view model observers
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

    companion object {
        fun newInstance() = GithubBrowserFragment()
    }
}
