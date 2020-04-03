package com.laurenyew.githubbrowser.repository

import com.laurenyew.githubbrowser.repository.models.GithubRepository
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryResponse
import com.laurenyew.githubbrowser.repository.networking.api.GithubApi
import com.laurenyew.githubbrowser.repository.networking.api.responses.GetGithubRespositoriesResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubBrowserRepository @Inject constructor(private val githubApi: GithubApi) {
    fun searchTopGithubRepositoriesByOrganization(organization: String?): Single<GithubRepositoryResponse> =
        githubApi
            .searchRepositories(organization ?: "")
            .doOnSubscribe {
                GithubRepositoryResponse.Loading
            }
            .map {
                parseGithubRepositoriesResponse(it)
            }
            .onErrorReturn { error -> GithubRepositoryResponse.Failure(error.message) }


    private fun parseGithubRepositoriesResponse(response: GetGithubRespositoriesResponse): GithubRepositoryResponse {
        val repos = arrayListOf<GithubRepository>()
        response.repos.forEach {
            repos.add(
                GithubRepository(
                    id = it.id,
                    name = it.name,
                    language = it.language,
                    description = it.description,
                    numStars = it.starCount
                )
            )
        }
        return GithubRepositoryResponse.Success(repos)
    }
}