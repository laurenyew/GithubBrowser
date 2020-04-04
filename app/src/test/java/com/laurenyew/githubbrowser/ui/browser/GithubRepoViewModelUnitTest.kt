package com.laurenyew.githubbrowser.ui.browser

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.laurenyew.githubbrowser.helpers.GithubRepoModelsResponseFactory
import com.laurenyew.githubbrowser.helpers.TestConstants.VALID_ORG_NAME
import com.laurenyew.githubbrowser.repository.GithubBrowserRepository
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepoModel
import com.laurenyew.githubbrowser.utils.SchedulersProvider
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class GithubRepoViewModelUnitTest {
    private val mockMainThread = newSingleThreadContext("Main thread")

    private var happyPathRepoResponse =
        GithubRepoModelsResponseFactory.createTestGithubRepoModelsResponseSuccess(3)

    private val happyPathRepoList = GithubRepoModelsResponseFactory.createTestGithubRepos(3)

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockRepository: GithubBrowserRepository

    @Mock
    private lateinit var mockSchedulerProvider: SchedulersProvider

    @Mock
    private lateinit var reposObserver: Observer<List<GithubRepoModel>>

    @Mock
    private lateinit var errorObserver: Observer<ErrorState?>

    private val testScheduler = TestScheduler()

    private lateinit var viewModel: GithubBrowserViewModel


    @Suppress("unused")
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mockMainThread)

        whenever(mockSchedulerProvider.io()).doReturn(testScheduler)
        whenever(mockSchedulerProvider.mainThread()).doReturn(testScheduler)

        viewModel = GithubBrowserViewModel(mockContext, mockRepository, mockSchedulerProvider)
        viewModel.githubRepos.observeForever(reposObserver)
        viewModel.errorState.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockMainThread.close()
    }

    @Test
    fun `initial setup`() {

    }

    @Test
    fun `searchGithubForTopReposBy valid organization`() {
        // Setup
        whenever(mockRepository.searchTopGithubRepositoriesByOrganization(VALID_ORG_NAME)).doReturn(
            Observable.just(happyPathRepoResponse)
        )

        // Exercise
        viewModel.searchGithubForTopReposBy(VALID_ORG_NAME)
        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        // Verify
        verify(reposObserver, times(1)).onChanged(
            argThat { equals(happyPathRepoList) }
        )
        verify(errorObserver, times(2)).onChanged(null)
    }

    @Test
    fun `searchGithubForTopReposBy invalid organization`() {

    }

    @Test
    fun `searchGithubForTopReposBy empty organization`() {

    }
}