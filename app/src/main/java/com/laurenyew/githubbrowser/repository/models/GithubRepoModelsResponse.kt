package com.laurenyew.githubbrowser.repository.models

sealed class GithubRepoModelsResponse {
    object Loading : GithubRepoModelsResponse()
    data class Failure(val errorState: ErrorState) : GithubRepoModelsResponse()
    data class Success(val result: List<GithubRepoModel>?) : GithubRepoModelsResponse()
}