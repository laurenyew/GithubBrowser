package com.laurenyew.githubbrowser.repository.models

sealed class GithubRepositoryResponse {
    data class Failure(val error: Exception) : GithubRepositoryResponse()
    data class Success(val result: List<GithubRepository>?) : GithubRepositoryResponse()
}