package com.laurenyew.githubbrowser.helpers

import com.laurenyew.githubbrowser.repository.models.GithubRepoModel
import com.laurenyew.githubbrowser.repository.models.GithubRepoModelsResponse

object GithubRepoModelsResponseFactory {
    fun createTestGithubRepoModelsResponseSuccess(numRepos: Int): GithubRepoModelsResponse =
        GithubRepoModelsResponse.Success(createTestGithubRepos(numRepos))

    fun createTestGithubRepos(numRepos: Int): List<GithubRepoModel> {
        val result = arrayListOf<GithubRepoModel>()
        for (i in 0..numRepos)
            result.add(
                GithubRepoModel(
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