package com.laurenyew.githubbrowser.repository

import com.laurenyew.githubbrowser.helpers.GithubRepositoryResponseFactory.createTestGithubRepositoryResponseSuccess
import com.laurenyew.githubbrowser.helpers.SearchGithubRepositoriesResponseFactory.createTestSearchGithubRepositoriesResponse
import com.laurenyew.githubbrowser.helpers.TestConstants.VALID_ORG_QUERY
import com.laurenyew.githubbrowser.repository.networking.api.GithubApi
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GithubBrowserRepositoryUnitTest {

    @Mock
    private lateinit var mockGithubApi: GithubApi
    private lateinit var repository: GithubBrowserRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = GithubBrowserRepository(mockGithubApi)
    }

    @Test
    fun `searchTopGithubRepositoriesByOrganization with valid organization, calls api, returns valid response`() {
        // Setup
        val validApiResponse = createTestSearchGithubRepositoriesResponse(3)
        whenever(mockGithubApi.searchRepositories(VALID_ORG_QUERY)).doReturn(
            Single.just(
                validApiResponse
            )
        )
        val validRepositoryResult = createTestGithubRepositoryResponseSuccess(3)


        // Exercise
        val testObserver = repository.searchTopGithubRepositoriesByOrganization("test").test()

        // Verify
        testObserver.assertValue(validRepositoryResult)

        // Cleanup
        testObserver.dispose()
    }
}