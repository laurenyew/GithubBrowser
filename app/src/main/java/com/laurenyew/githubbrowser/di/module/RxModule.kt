package com.laurenyew.githubbrowser.di.module

import com.laurenyew.githubbrowser.utils.AppSchedulersProvider
import com.laurenyew.githubbrowser.utils.SchedulersProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RxModule {
    @Singleton
    @Provides
    fun provideSchedulersProvider(): SchedulersProvider =
        AppSchedulersProvider()
}