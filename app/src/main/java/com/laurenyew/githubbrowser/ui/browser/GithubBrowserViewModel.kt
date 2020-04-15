package com.laurenyew.githubbrowser.ui.browser

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurenyew.githubbrowser.repository.GithubBrowserRepository
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepoModel
import com.laurenyew.githubbrowser.repository.models.GithubRepoModelsResponse
import com.laurenyew.githubbrowser.ui.detail.GithubRepoDetailActivity
import com.laurenyew.githubbrowser.ui.utils.CustomChromeTabsHelperUtil
import com.laurenyew.githubbrowser.utils.SchedulersProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * ViewModel (MVVM) for Github Browser Feature
 */
class GithubBrowserViewModel @Inject constructor(
    private val context: Context?,
    private val repository: GithubBrowserRepository,
    private val schedulersProvider: SchedulersProvider
) : ViewModel() {
    private val isGoogleChromeTabsSupported =
        CustomChromeTabsHelperUtil.isChromeCustomTabsSupported(context)
    private val disposable = CompositeDisposable()

    // Private live data variables
    private val githubReposLiveData: MutableLiveData<List<GithubRepoModel>> by lazy {
        MutableLiveData<List<GithubRepoModel>>()
    }
    private val errorStateLiveData: MutableLiveData<ErrorState?> by lazy {
        MutableLiveData<ErrorState?>()
    }
    private val isLoadingLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private var shouldPreferChromeTab: Boolean = true

    init {
        val preferChromeTab =
            context?.getSharedPreferences(REPO_DETAILS_TYPE_SHARED_PREF, MODE_PRIVATE)
                ?.getBoolean(REPO_DETAILS_TYPE_SHARED_PREF_KEY, true)
        preferChromeTab?.let { shouldPreferChromeTab = it }
    }


    // View Model available live data variables (used by View)
    val isLoading: LiveData<Boolean> = isLoadingLiveData
    val githubRepos: LiveData<List<GithubRepoModel>> = githubReposLiveData
    val errorState: LiveData<ErrorState?> = errorStateLiveData

    /**
     * When the View Model is destroyed,
     * clean up RxJava disposable and clear the chrome tab session
     */
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        CustomChromeTabsHelperUtil.clearChromeTabs()
    }

    fun setShouldPreferChromeTab(preferChromeTab: Boolean) {
        shouldPreferChromeTab = preferChromeTab
        context?.getSharedPreferences(REPO_DETAILS_TYPE_SHARED_PREF, MODE_PRIVATE)
            ?.edit()
            ?.putBoolean(REPO_DETAILS_TYPE_SHARED_PREF_KEY, preferChromeTab)
            ?.apply()
    }

    /**
     * Call the repository for search results for organization name
     * (Updates the view model live data with the results)
     */
    fun searchGithubForTopReposBy(organizationName: String) {
        disposable.add(repository.searchTopGithubRepositoriesByOrganization(organizationName.trim())
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.mainThread())
            .doOnSubscribe {
                handleResponse(GithubRepoModelsResponse.Loading)
            }
            .subscribe { output ->
                handleResponse(output)
            }
        )
    }

    /**
     * Attempt to open repo details with Google Chrome Custom Tabs (if supported)
     * if not, use a WebView activity
     */
    fun openRepoDetails(websiteUrl: String) {
        context?.let {
            if (isGoogleChromeTabsSupported && shouldPreferChromeTab) {
                CustomChromeTabsHelperUtil.openCustomChromeTab(context, websiteUrl)
            } else {
                val intent = Intent(context, GithubRepoDetailActivity::class.java).apply {
                    putExtra(GithubRepoDetailActivity.WEBSITE_URL_KEY, websiteUrl)
                    flags = FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
        }
    }

    /**
     * Handle the Loading / Success / Failure responses, updating the
     * LiveData appropriately
     */
    private fun handleResponse(response: GithubRepoModelsResponse) {
        when (response) {
            is GithubRepoModelsResponse.Loading -> {
                githubReposLiveData.value = emptyList()
                errorStateLiveData.value = null
                isLoadingLiveData.value = true
            }
            is GithubRepoModelsResponse.Failure -> {
                githubReposLiveData.value = emptyList()
                errorStateLiveData.value = response.errorState
                isLoadingLiveData.value = false
            }
            is GithubRepoModelsResponse.Success -> {
                val result = response.result
                githubReposLiveData.value = result
                errorStateLiveData.value = null
                isLoadingLiveData.value = false
                if (isGoogleChromeTabsSupported) {
                    CustomChromeTabsHelperUtil.warmupChromeTabs(
                        context,
                        result?.map { it.websiteUrl })
                }
            }
        }
    }

    companion object {
        private const val REPO_DETAILS_TYPE_SHARED_PREF = "repo_details_type"
        private const val REPO_DETAILS_TYPE_SHARED_PREF_KEY = "prefer_chrome_key"
    }
}
