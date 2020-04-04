package com.laurenyew.githubbrowser.di.component

import android.app.Application
import com.laurenyew.githubbrowser.BaseApplication
import com.laurenyew.githubbrowser.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        FragmentBindingModule::class,
        ContextModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: BaseApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application?): Builder?
        fun build(): ApplicationComponent?
    }
}