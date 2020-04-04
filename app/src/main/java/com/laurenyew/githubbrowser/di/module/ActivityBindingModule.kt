package com.laurenyew.githubbrowser.di.module

import com.laurenyew.githubbrowser.ui.GithubBrowserActivity
import com.laurenyew.githubbrowser.ui.detail.GithubRepoDetailActivity
import dagger.Module

import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun bindGithubBrowserActivity(): GithubBrowserActivity?

    @ContributesAndroidInjector
    abstract fun bindGithubRepoDetailActivity(): GithubRepoDetailActivity?
}