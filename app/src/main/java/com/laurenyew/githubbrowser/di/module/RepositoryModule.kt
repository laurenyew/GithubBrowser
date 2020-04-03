package com.laurenyew.githubbrowser.di.module

import com.laurenyew.githubbrowser.repository.GithubBrowserRepository
import com.laurenyew.githubbrowser.repository.networking.api.GithubApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideGithubBrowserRepository(githubApi: GithubApi): GithubBrowserRepository =
        GithubBrowserRepository(githubApi)
}