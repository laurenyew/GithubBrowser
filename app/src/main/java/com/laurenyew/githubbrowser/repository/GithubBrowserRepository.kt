package com.laurenyew.githubbrowser.repository

import android.util.MalformedJsonException
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryModel
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryResponse
import com.laurenyew.githubbrowser.repository.networking.api.GithubApi
import com.laurenyew.githubbrowser.repository.networking.api.responses.GithubRepository
import io.reactivex.Single
import retrofit2.HttpException
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
                parseGithubRepositoriesResponseSuccess(it)
            }
            .onErrorReturn { error ->
                parseGithubRepositoriesResponseError(error)
            }


    private fun parseGithubRepositoriesResponseSuccess(response: Array<GithubRepository>): GithubRepositoryResponse {
        val repos = arrayListOf<GithubRepositoryModel>()
        response.forEach {
            repos.add(
                GithubRepositoryModel(
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

    private fun parseGithubRepositoriesResponseError(exception: Throwable): GithubRepositoryResponse {
        val errorState = when (exception) {
            is HttpException -> ErrorState.NetworkError
            is MalformedJsonException -> ErrorState.MalformedResultError
            else -> ErrorState.UnknownError(exception.message)
        }
        return GithubRepositoryResponse.Failure(errorState)
    }
}