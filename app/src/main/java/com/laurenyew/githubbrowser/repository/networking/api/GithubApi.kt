package com.laurenyew.githubbrowser.repository.networking.api

import com.laurenyew.githubbrowser.repository.networking.api.responses.GithubRepository
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("/orgs/{organizationName}/repos")
    fun searchRepositories(
        @Path("organizationName") organizationName: String,
        @Query("sort") sortType: String = "stars",
        @Query("order") order: String = "desc"
    ): Single<Array<GithubRepository>>
}