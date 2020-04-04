package com.laurenyew.githubbrowser.helpers

import com.laurenyew.githubbrowser.repository.models.GithubRepositoryModel
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryResponse

object GithubRepositoryResponseFactory {
    fun createTestGithubRepositoryResponseSuccess(numRepos: Int): GithubRepositoryResponse =
        GithubRepositoryResponse.Success(createTestGithubRepos(numRepos))

    fun createTestGithubRepos(numRepos: Int): List<GithubRepositoryModel> {
        val result = arrayListOf<GithubRepositoryModel>()
        for (i in 0..numRepos)
            result.add(
                GithubRepositoryModel(
                    id = i.toString(),
                    name = "name${i}",
                    description = "description${i}",
                    websiteUrl = "www.google.com/${i}",
                    numStars = i,
                    language = "Kotlin"
                )
            )

        return result
    }
}