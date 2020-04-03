package com.laurenyew.githubbrowser.ui.browser.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.laurenyew.githubbrowser.R
import com.laurenyew.githubbrowser.repository.models.GithubRepository
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class GithubBrowserRecyclerViewAdapter : RecyclerView.Adapter<GithubRepoPreviewViewHolder>(),
    CoroutineScope {

    private val job = Job()
    private var data: MutableList<GithubRepository> = ArrayList()
    private var pendingDataUpdates = ArrayDeque<List<GithubRepository>>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    //RecyclerView Diff.Util (List Updates)
    fun updateData(newData: List<GithubRepository>?) {
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
    private fun updateDataInternal(newData: List<GithubRepository>?) {
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
        newData: List<GithubRepository>?,
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
        oldData: List<GithubRepository>?,
        newData: List<GithubRepository>?
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
        holder.languageTextView.text = item.language
        holder.starCountTextView.text =
            context.getString(R.string.github_repo_star_count, item.numStars)
    }

    override fun getItemCount(): Int = data.size
//endregion
}