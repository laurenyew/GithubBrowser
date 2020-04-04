package com.laurenyew.githubbrowser.repository

import android.util.MalformedJsonException
import com.laurenyew.githubbrowser.helpers.GithubRepoModelsResponseFactory.createTestGithubRepoModelsResponseSuccess
import com.laurenyew.githubbrowser.helpers.SearchGithubReposResponseFactory.createTestSearchGithubRepositoriesResponse
import com.laurenyew.githubbrowser.helpers.TestConstants.INVALID_ORG_NAME
import com.laurenyew.githubbrowser.helpers.TestConstants.INVALID_ORG_QUERY
import com.laurenyew.githubbrowser.helpers.TestConstants.VALID_ORG_NAME
import com.laurenyew.githubbrowser.helpers.TestConstants.VALID_ORG_QUERY
import com.laurenyew.githubbrowser.repository.GithubBrowserRepository.Companion.INVALID_QUERY_ERROR_CODE
import com.laurenyew.githubbrowser.repository.GithubBrowserRepository.Companion.RATE_LIMIT_ERROR_CODE
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepoModelsResponse
import com.laurenyew.githubbrowser.repository.networking.api.GithubApi
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response

/**
 * Unit tests for [GithubBrowserRepository] features
 */
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
        whenever(mockGithubApi.searchRepos(VALID_ORG_QUERY)).doReturn(
            Single.just(
                validApiResponse
            )
        )
        val validRepositoryResult = createTestGithubRepoModelsResponseSuccess(3)

        // Exercise
        val testObserver =
            repository.searchTopGithubRepositoriesByOrganization(VALID_ORG_NAME).test()

        // Verify
        verify(mockGithubApi).searchRepos(VALID_ORG_QUERY)
        testObserver.assertValue(validRepositoryResult)

        // Cleanup
        testObserver.dispose()
    }

    @Test
    fun `searchTopGithubRepositoriesByOrganization with invalid organization, calls api, returns faliure response`() {
        // Setup
        whenever(mockGithubApi.searchRepos(INVALID_ORG_QUERY)).doReturn(
            Single.error(MalformedJsonException("invalid JSON"))
        )
        val invalidRepositoryResult =
            GithubRepoModelsResponse.Failure(ErrorState.MalformedResultError)

        // Exercise
        val testObserver =
            repository.searchTopGithubRepositoriesByOrganization(INVALID_ORG_NAME).test()

        // Verify
        verify(mockGithubApi).searchRepos(INVALID_ORG_QUERY)
        testObserver.assertValue(invalidRepositoryResult)

        // Cleanup
        testObserver.dispose()
    }

    @Test
    fun `parseGithubRepositoriesResponseError HitRateLimitError returns the appropriate error state`() {
        // Setup
        val response: Response<Any> = mock()
        whenever(response.code()).doReturn(RATE_LIMIT_ERROR_CODE)
        val exception = HttpException(response)

        // Exercise
        val result = repository.parseGithubRepositoriesResponseError(exception)


        // Verify
        assertTrue(result is GithubRepoModelsResponse.Failure)
        val errorState = (result as GithubRepoModelsResponse.Failure).errorState
        assertEquals(ErrorState.HitRateLimitError, errorState)
    }

    @Test
    fun `parseGithubRepositoriesResponseError InvalidQueryError returns the appropriate error state`() {
        // Setup
        val response: Response<Any> = mock()
        whenever(response.code()).doReturn(INVALID_QUERY_ERROR_CODE)
        val exception = HttpException(response)

        // Exercise
        val result = repository.parseGithubRepositoriesResponseError(exception)


        // Verify
        assertTrue(result is GithubRepoModelsResponse.Failure)
        val errorState = (result as GithubRepoModelsResponse.Failure).errorState
        assertEquals(ErrorState.InvalidQueryError, errorState)
    }

    @Test
    fun `parseGithubRepositoriesResponseError NetworkError returns the appropriate error state`() {
        // Setup
        val response: Response<Any> = mock()
        whenever(response.code()).doReturn(404)
        val exception = HttpException(response)

        // Exercise
        val result = repository.parseGithubRepositoriesResponseError(exception)


        // Verify
        assertTrue(result is GithubRepoModelsResponse.Failure)
        val errorState = (result as GithubRepoModelsResponse.Failure).errorState
        assertEquals(ErrorState.NetworkError, errorState)
    }

    @Test
    fun `parseGithubRepositoriesResponseError MalformedResultError returns the appropriate error state`() {
        // Setup
        val exception = MalformedJsonException("test")


        // Exercise
        val result = repository.parseGithubRepositoriesResponseError(exception)


        // Verify
        assertTrue(result is GithubRepoModelsResponse.Failure)
        val errorState = (result as GithubRepoModelsResponse.Failure).errorState
        assertEquals(ErrorState.MalformedResultError, errorState)
    }

    @Test
    fun `parseGithubRepositoriesResponseError UnknownError returns the appropriate error state`() {
        // Setup
        val exception = RuntimeException("test")

        // Exercise
        val result = repository.parseGithubRepositoriesResponseError(exception)

        // Verify
        assertTrue(result is GithubRepoModelsResponse.Failure)
        val errorState = (result as GithubRepoModelsResponse.Failure).errorState
        assertTrue(errorState is ErrorState.UnknownError)
    }
}