package com.laurenyew.githubbrowser.ui.browser.views

import androidx.recyclerview.widget.DiffUtil
import com.laurenyew.githubbrowser.repository.models.GithubRepoModel

/**
 * DiffUtil.Callback to differentiate one item from another
 */
open class GithubRepoDataDiffCallback(
    private val oldData: List<GithubRepoModel>?,
    private val newData: List<GithubRepoModel>?
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldData?.size ?: 0

    override fun getNewListSize(): Int = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData?.get(oldItemPosition)
        val newItem = newData?.get(newItemPosition)

        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData?.get(oldItemPosition)
        val newItem = newData?.get(newItemPosition)

        return oldItem?.name == newItem?.name
                && oldItem?.description == newItem?.description
                && oldItem?.language == newItem?.language
                && oldItem?.numStars == newItem?.numStars

    }
}