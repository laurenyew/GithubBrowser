package com.laurenyew.githubbrowser.repository.networking.api.responses

import com.squareup.moshi.Json

data class GetGithubRespositoriesResponse(
    @Json(name = "items") val repos: List<GithubRepo>
)

data class GithubRepo(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "description") val description: String,
    @Json(name = "url") val url: String,
    @Json(name = "stargazers_count") val starCount: Int,
    @Json(name = "language") val language: String
)