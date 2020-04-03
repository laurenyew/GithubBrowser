package com.laurenyew.githubbrowser.repository.models

sealed class GithubRepositoryResponse {
    object Loading : GithubRepositoryResponse()
    data class Failure(val errorMessage: String?) : GithubRepositoryResponse()
    data class Success(val result: List<GithubRepository>?) : GithubRepositoryResponse()
}