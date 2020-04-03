package com.laurenyew.githubbrowser.ui.browser.views

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.laurenyew.githubbrowser.R

class GithubRepoPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val rankingTextView: TextView = view.findViewById(R.id.repo_ranking)
    val nameTextView: TextView = view.findViewById(R.id.repo_name)
    val descriptionTextView: TextView = view.findViewById(R.id.repo_description)
    val languageTextView: TextView = view.findViewById(R.id.repo_language)
    val starCountTextView: TextView = view.findViewById(R.id.repo_star_count)
}