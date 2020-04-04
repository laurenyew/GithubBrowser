package com.laurenyew.githubbrowser.repository

import android.util.MalformedJsonException
import com.laurenyew.githubbrowser.helpers.GithubRepositoryResponseFactory.createTestGithubRepositoryResponseSuccess
import com.laurenyew.githubbrowser.helpers.SearchGithubRepositoriesResponseFactory.createTestSearchGithubRepositoriesResponse
import com.laurenyew.githubbrowser.helpers.TestConstants.INVALID_ORG_NAME
import com.laurenyew.githubbrowser.helpers.TestConstants.INVALID_ORG_QUERY
import com.laurenyew.githubbrowser.helpers.TestConstants.VALID_ORG_NAME
import com.laurenyew.githubbrowser.helpers.TestConstants.VALID_ORG_QUERY
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryResponse
import com.laurenyew.githubbrowser.repository.networking.api.GithubApi
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
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
    fun `searchTopGithubRepositoriesByOrganization with valid organization, calls api, returns success response`() {
        // Setup
        val validApiResponse = createTestSearchGithubRepositoriesResponse(3)
        whenever(mockGithubApi.searchRepositories(VALID_ORG_QUERY)).doReturn(
            Single.just(
                validApiResponse
            )
        )
        val validRepositoryResult = createTestGithubRepositoryResponseSuccess(3)

        // Exercise
        val testObserver =
            repository.searchTopGithubRepositoriesByOrganization(VALID_ORG_NAME).test()

        // Verify
        verify(mockGithubApi).searchRepositories(VALID_ORG_QUERY)
        testObserver.assertValue(validRepositoryResult)

        // Cleanup
        testObserver.dispose()
    }

    @Test
    fun `searchTopGithubRepositoriesByOrganization with invalid organization, calls api, returns faliure response`() {
        // Setup
        whenever(mockGithubApi.searchRepositories(INVALID_ORG_QUERY)).doReturn(
            Single.error(MalformedJsonException("invalid JSON"))
        )
        val invalidRepositoryResult =
            GithubRepositoryResponse.Failure(ErrorState.MalformedResultError)

        // Exercise
        val testObserver =
            repository.searchTopGithubRepositoriesByOrganization(INVALID_ORG_NAME).test()

        // Verify
        verify(mockGithubApi).searchRepositories(INVALID_ORG_QUERY)
        testObserver.assertValue(invalidRepositoryResult)

        // Cleanup
        testObserver.dispose()
    }
}