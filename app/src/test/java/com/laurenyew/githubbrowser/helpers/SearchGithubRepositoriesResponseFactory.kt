package com.laurenyew.githubbrowser.helpers

import com.laurenyew.githubbrowser.repository.networking.api.responses.GithubRepository
import com.laurenyew.githubbrowser.repository.networking.api.responses.SearchGithubRepositoriesResponse

object SearchGithubRepositoriesResponseFactory {
    fun createTestSearchGithubRepositoriesResponse(numRepos: Int): SearchGithubRepositoriesResponse =
        SearchGithubRepositoriesResponse(numRepos, createTestGithubRepos(numRepos))

    fun createTestGithubRepos(numRepos: Int): Array<GithubRepository> {
        val result = arrayListOf<GithubRepository>()
        for (i in 0..numRepos)
            result.add(
                GithubRepository(
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