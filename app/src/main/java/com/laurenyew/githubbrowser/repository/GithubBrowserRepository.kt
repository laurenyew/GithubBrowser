package com.laurenyew.githubbrowser.repository

import android.util.MalformedJsonException
import androidx.annotation.VisibleForTesting
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryModel
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryResponse
import com.laurenyew.githubbrowser.repository.networking.api.GithubApi
import com.laurenyew.githubbrowser.repository.networking.api.responses.SearchGithubRepositoriesResponse
import io.reactivex.Observable
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubBrowserRepository @Inject constructor(private val githubApi: GithubApi) {
    fun searchTopGithubRepositoriesByOrganization(organizationName: String?): Observable<GithubRepositoryResponse> =
        githubApi
            .searchRepositories(createSearchRepositoriesQuery(organizationName))
            .map {
                parseGithubRepositoriesResponseSuccess(it)
            }
            .onErrorReturn { error ->
                parseGithubRepositoriesResponseError(error)
            }
            .toObservable()

    private fun createSearchRepositoriesQuery(organizationName: String?): String =
        if (organizationName != null) {
            ORG_QUERY + organizationName
        } else {
            ""
        }


    private fun parseGithubRepositoriesResponseSuccess(response: SearchGithubRepositoriesResponse): GithubRepositoryResponse {
        val repos = arrayListOf<GithubRepositoryModel>()
        response.items.forEach {
            repos.add(
                GithubRepositoryModel(
                    id = it.id,
                    name = it.name,
                    language = it.language,
                    description = it.description,
                    numStars = it.starCount,
                    websiteUrl = it.websiteUrl
                )
            )
        }
        return GithubRepositoryResponse.Success(repos)
    }

    @VisibleForTesting
    fun parseGithubRepositoriesResponseError(exception: Throwable): GithubRepositoryResponse {
        val errorState = when (exception) {
            is HttpException ->
                when (exception.code()) {
                    INVALID_QUERY_ERROR_CODE -> ErrorState.InvalidQueryError
                    RATE_LIMIT_ERROR_CODE -> ErrorState.HitRateLimitError
                    else -> ErrorState.NetworkError
                }
            is MalformedJsonException -> ErrorState.MalformedResultError
            else -> ErrorState.UnknownError(exception.message)
        }
        return GithubRepositoryResponse.Failure(errorState)
    }

    companion object {
        @VisibleForTesting
        const val INVALID_QUERY_ERROR_CODE = 422
        @VisibleForTesting
        const val RATE_LIMIT_ERROR_CODE = 403
        private const val ORG_QUERY = "org:"
    }
}