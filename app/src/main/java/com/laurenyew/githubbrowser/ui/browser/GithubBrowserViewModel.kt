package com.laurenyew.githubbrowser.ui.browser

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurenyew.githubbrowser.repository.GithubBrowserRepository
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryModel
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryResponse
import com.laurenyew.githubbrowser.ui.detail.GithubRepoDetailActivity
import com.laurenyew.githubbrowser.ui.utils.CustomTabsHelperUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class GithubBrowserViewModel @Inject constructor(
    private val context: Context?,
    private val repository: GithubBrowserRepository
) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val githubReposLiveData: MutableLiveData<List<GithubRepositoryModel>> by lazy {
        MutableLiveData<List<GithubRepositoryModel>>()
    }
    private val errorStateLiveData: MutableLiveData<ErrorState?> by lazy {
        MutableLiveData<ErrorState?>()
    }
    private val isLoadingLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isLoading: LiveData<Boolean> = isLoadingLiveData
    val githubRepos: LiveData<List<GithubRepositoryModel>> = githubReposLiveData
    val errorState: LiveData<ErrorState?> = errorStateLiveData

    fun searchGithubForTopReposBy(organizationName: String) {
        disposable.add(repository.searchTopGithubRepositoriesByOrganization(organizationName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                handleResponse(GithubRepositoryResponse.Loading)
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
            if (CustomTabsHelperUtil.isChromeCustomTabsSupported(context)) {
                CustomTabsHelperUtil.openCustomChromeTab(context, websiteUrl)
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
    private fun handleResponse(response: GithubRepositoryResponse) {
        when (response) {
            is GithubRepositoryResponse.Loading -> {
                githubReposLiveData.value = emptyList()
                errorStateLiveData.value = null
                isLoadingLiveData.value = true
            }
            is GithubRepositoryResponse.Failure -> {
                githubReposLiveData.value = emptyList()
                errorStateLiveData.value = response.errorState
                isLoadingLiveData.value = false
            }
            is GithubRepositoryResponse.Success -> {
                githubReposLiveData.value = response.result
                errorStateLiveData.value = null
                isLoadingLiveData.value = false
            }
        }
    }
}
