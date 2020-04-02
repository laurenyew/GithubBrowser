package com.laurenyew.githubbrowser.repository

import androidx.lifecycle.MutableLiveData
import com.laurenyew.githubbrowser.repository.models.GithubRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class GithubBrowserRepository @Inject constructor() {
    val githubRepositoriesLiveData: MutableLiveData<List<GithubRepository>> by lazy {
        MutableLiveData<List<GithubRepository>>()
    }

    private val disposables = CompositeDisposable()


    fun searchTopGithubRepositoriesByOrganization(organization: String) {
        disposables
    }
}