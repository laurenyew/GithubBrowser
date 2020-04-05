package com.laurenyew.githubbrowser.repository.models

/**
 * POJO data model for Github Repos used in business logic
 */
data class GithubRepoModel(
    val id: String,
    val name: String,
    val language: String?,
    val numStars: Int,
    val description: String?,
    val websiteUrl: String
)