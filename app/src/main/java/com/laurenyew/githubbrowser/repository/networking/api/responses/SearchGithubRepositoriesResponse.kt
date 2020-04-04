package com.laurenyew.githubbrowser.repository.networking.api.responses

import com.google.gson.annotations.SerializedName

data class SearchGithubRepositoriesResponse(
    @SerializedName("total_count") val totalCount: Int,
    val items: Array<GithubRepository>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchGithubRepositoriesResponse

        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        return items.contentHashCode()
    }
}

data class GithubRepository(
    val id: String,
    val name: String,
    val description: String?,
    @SerializedName("html_url") val websiteUrl: String,
    @SerializedName("stargazers_count") val starCount: Int,
    val language: String?
)