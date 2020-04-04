package com.laurenyew.githubbrowser.helpers

import com.laurenyew.githubbrowser.repository.networking.api.responses.GithubRepo
import com.laurenyew.githubbrowser.repository.networking.api.responses.SearchGithubReposResponse

object SearchGithubReposResponseFactory {
    fun createTestSearchGithubRepositoriesResponse(numRepos: Int): SearchGithubReposResponse =
        SearchGithubReposResponse(numRepos, createTestGithubRepos(numRepos))

    fun createTestGithubRepos(numRepos: Int): Array<GithubRepo> {
        val result = arrayListOf<GithubRepo>()
        for (i in 0..numRepos)
            result.add(
                GithubRepo(
                    id = i.toString(),
                    name = "name${i}",
                    description = "description${i}",
                    websiteUrl = "www.google.com/${i}",
                    starCount = i,
                    language = "Kotlin"
                )
            )

        return result.toTypedArray()
    }
}