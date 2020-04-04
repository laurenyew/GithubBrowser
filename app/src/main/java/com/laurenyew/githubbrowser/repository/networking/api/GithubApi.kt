package com.laurenyew.githubbrowser.repository.networking.api

import com.laurenyew.githubbrowser.repository.networking.api.responses.SearchGithubReposResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("/search/repositories")
    fun searchRepos(
        @Query("q") query: String,
        @Query("sort") sortType: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") numItemsPerPage: Int = 3
    ): Single<SearchGithubReposResponse>
}