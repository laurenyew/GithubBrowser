package com.laurenyew.githubbrowser.ui.browser.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.laurenyew.githubbrowser.R
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryModel
import com.laurenyew.githubbrowser.ui.browser.GithubBrowserViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class GithubBrowserRecyclerViewAdapter(private val onItemClicked: ((GithubRepositoryModel) -> Unit)) :
    RecyclerView.Adapter<GithubRepoPreviewViewHolder>(),
    CoroutineScope {

    @Inject
    lateinit var viewModel: GithubBrowserViewModel

    private val job = Job()
    private var data: MutableList<GithubRepositoryModel> = ArrayList()
    private var pendingDataUpdates = ArrayDeque<List<GithubRepositoryModel>>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    //RecyclerView Diff.Util (List Updates)
    fun updateData(newData: List<GithubRepositoryModel>?) {
        if (isActive) {
            val data = newData ?: ArrayList()
            pendingDataUpdates.add(data)
            if (pendingDataUpdates.size <= 1) {
                updateDataInternal(data)
            }
        }
    }

    //If the adapter is destroyed, cancel any running jobs
    fun onDestroy() {
        job.cancel()
        pendingDataUpdates.clear()
    }

    /**
     * Handle the diff util update on a background thread
     * (this can take O(n) time so we don't want it on the main thread)
     */
    private fun updateDataInternal(newData: List<GithubRepositoryModel>?) {
        val oldData = ArrayList(data)

        launch {
            val diffCallback = createDataDiffCallback(oldData, newData)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            if (isActive) {
                withContext(Dispatchers.Main) {
                    applyDataDiffResult(newData, diffResult)
                }
            }
        }
    }

    /**
     * UI thread callback to apply the diff result to the adapter
     * and take in the latest update
     */
    private fun applyDataDiffResult(
        newData: List<GithubRepositoryModel>?,
        diffResult: DiffUtil.DiffResult
    ) {
        if (pendingDataUpdates.isNotEmpty()) {
            pendingDataUpdates.remove()
        }

        //Apply the data to the view
        data.clear()
        if (newData != null) {
            data.addAll(newData)
        }
        diffResult.dispatchUpdatesTo(this)

        //Take in the next latest update
        if (pendingDataUpdates.isNotEmpty()) {
            val latestDataUpdate = pendingDataUpdates.pop()
            pendingDataUpdates.clear()
            updateDataInternal(latestDataUpdate)
        }
    }

    private fun createDataDiffCallback(
        oldData: List<GithubRepositoryModel>?,
        newData: List<GithubRepositoryModel>?
    ): DiffUtil.Callback =
        GithubRepoDataDiffCallback(oldData, newData)
    //endregion

    //region RecyclerView.Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubRepoPreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.github_browser_preview_view, parent, false)
        return GithubRepoPreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: GithubRepoPreviewViewHolder, position: Int) {
        val item = data[position]
        val context = holder.itemView.context
        holder.rankingTextView.text = (position + 1).toString()
        holder.nameTextView.text = item.name
        holder.descriptionTextView.text = item.description ?: ""
        val language = item.language
        holder.languageTextView.text = if (language.isNullOrBlank()) {
            context.getString(R.string.unknown)
        } else {
            language
        }

        holder.starCountTextView.text =
            context.getString(R.string.github_repo_star_count, item.numStars)

        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun getItemCount(): Int = data.size
//endregion
}