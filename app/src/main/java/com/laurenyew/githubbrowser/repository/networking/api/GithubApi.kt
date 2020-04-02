package com.laurenyew.githubbrowser.repository.networking.api

import com.laurenyew.githubbrowser.repository.networking.api.responses.GetGithubRespositoriesResponse
import retrofit2.Call
import retrofit2.http.GET

interface GithubApi {
    @GET("search/repositories")
    fun searchRepositories(): Call<GetGithubRespositoriesResponse>?
}