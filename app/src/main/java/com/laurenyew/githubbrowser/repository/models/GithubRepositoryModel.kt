package com.laurenyew.githubbrowser.repository.models

data class GithubRepositoryModel(
    val id: String,
    val name: String,
    val language: String?,
    val numStars: Int,
    val description: String?
)