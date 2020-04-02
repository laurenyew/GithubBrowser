package com.laurenyew.githubbrowser.di.module

import com.laurenyew.githubbrowser.MainActivity
import dagger.Module

import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun bindMainActivity(): MainActivity?
}