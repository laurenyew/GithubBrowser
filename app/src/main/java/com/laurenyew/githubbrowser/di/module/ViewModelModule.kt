package com.laurenyew.githubbrowser.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.laurenyew.githubbrowser.di.keys.ViewModelKey
import com.laurenyew.githubbrowser.ui.ViewModelFactory
import com.laurenyew.githubbrowser.ui.browser.GithubBrowserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GithubBrowserViewModel::class)
    abstract fun bindGithubBrowserViewModel(githubBrowserViewModel: GithubBrowserViewModel): ViewModel

// TODO   @Binds
//    @IntoMap
//    @ViewModelKey(GithubRepoDetailsViewModel::class)
//    abstract fun bindGithubRepoDetailsViewModel(detailsViewModel: GithubRepoDetailsViewModel?): ViewModel?

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}