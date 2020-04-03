package com.laurenyew.githubbrowser.di.module

import com.laurenyew.githubbrowser.ui.browser.GithubBrowserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {
    @ContributesAndroidInjector
    abstract fun bindGithubBrowserFragment(): GithubBrowserFragment

// TODO    @ContributesAndroidInjector
//    abstract fun provideRepoDetailsFragment(): GithubRepoDetailsFragment?
}