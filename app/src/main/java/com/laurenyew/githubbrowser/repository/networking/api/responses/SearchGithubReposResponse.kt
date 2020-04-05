package com.laurenyew.githubbrowser.repository.networking.api.responses

import com.google.gson.annotations.SerializedName

/**
 * Network JSON response model for Search Github Repos
 */
data class SearchGithubReposResponse(
    @SerializedName("total_count") val totalCount: Int,
    val items: Array<GithubRepo>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchGithubReposResponse

        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        return items.contentHashCode()
    }
}

data class GithubRepo(
    val id: String,
    val name: String,
    val description: String?,
    @SerializedName("html_url") val websiteUrl: String,
    @SerializedName("stargazers_count") val starCount: Int,
    val language: String?
)