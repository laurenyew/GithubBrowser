package com.laurenyew.githubbrowser.repository.networking.api

import com.laurenyew.githubbrowser.repository.networking.api.responses.GetGithubRespositoriesResponse
import io.reactivex.Single
import retrofit2.http.GET

interface GithubApi {
    @GET("search/repositories")
    fun searchRepositories(organizationName: String): Single<GetGithubRespositoriesResponse>
}