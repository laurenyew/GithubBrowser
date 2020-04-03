package com.laurenyew.githubbrowser.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurenyew.githubbrowser.repository.GithubBrowserRepository
import com.laurenyew.githubbrowser.repository.models.ErrorState
import com.laurenyew.githubbrowser.repository.models.GithubRepository
import com.laurenyew.githubbrowser.repository.models.GithubRepositoryResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GithubBrowserViewModel @Inject constructor(
    private val repository: GithubBrowserRepository
) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val githubReposLiveData: MutableLiveData<List<GithubRepository>> by lazy {
        MutableLiveData<List<GithubRepository>>()
    }
    private val errorStateLiveData: MutableLiveData<ErrorState?> by lazy {
        MutableLiveData<ErrorState?>()
    }
    private val isLoadingLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isLoading: LiveData<Boolean> = isLoadingLiveData
    val githubRepos: LiveData<List<GithubRepository>> = githubReposLiveData
    val errorState: LiveData<ErrorState?> = errorStateLiveData


    fun searchGithubForTopReposBy(organizationName: String) {
        disposable.add(repository.searchTopGithubRepositoriesByOrganization(organizationName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { output ->
                when (output) {
                    is GithubRepositoryResponse.Loading -> {
                        isLoadingLiveData.value = true
                        githubReposLiveData.value = emptyList()
                        errorStateLiveData.value = null
                    }
                    is GithubRepositoryResponse.Failure -> {
                        isLoadingLiveData.value = false
                        githubReposLiveData.value = emptyList()
                        errorStateLiveData.value = ErrorState.UnknownError(output.errorMessage)
                    }
                    is GithubRepositoryResponse.Success -> {
                        isLoadingLiveData.value = false
                        githubReposLiveData.value = output.result
                        errorStateLiveData.value = null
                    }
                }
            }
        )
    }
}
