package com.laurenyew.githubbrowser.repository.models

sealed class GithubRepositoryResponse {
    object Loading : GithubRepositoryResponse()
    data class Failure(val errorState: ErrorState) : GithubRepositoryResponse()
    data class Success(val result: List<GithubRepositoryModel>?) : GithubRepositoryResponse()
}