package com.laurenyew.githubbrowser.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulersProvider {
    fun io(): Scheduler
    fun mainThread(): Scheduler
}

class AppSchedulersProvider : SchedulersProvider {
    override fun io() = Schedulers.io()
    override fun mainThread() = AndroidSchedulers.mainThread()
}