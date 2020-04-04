package com.laurenyew.githubbrowser.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Used to make RxJava Schedulers more testable
 */
interface SchedulersProvider {
    fun io(): Scheduler
    fun mainThread(): Scheduler
}

class AppSchedulersProvider : SchedulersProvider {
    override fun io(): Scheduler = Schedulers.io()
    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()
}