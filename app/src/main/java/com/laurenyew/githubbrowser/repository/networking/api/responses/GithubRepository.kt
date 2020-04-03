package com.laurenyew.githubbrowser.repository.networking.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubRepository(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    @Json(name = "stargazers_count") val starCount: Int,
    val language: String
)