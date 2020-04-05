package com.laurenyew.githubbrowser

import com.laurenyew.githubbrowser.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Application that sets up Dagger
 */
class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerApplicationComponent.builder().application(this)?.build()
        component?.inject(this)
        return component!!
    }
}