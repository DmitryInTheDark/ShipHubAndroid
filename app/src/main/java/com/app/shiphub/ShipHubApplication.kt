package com.app.shiphub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ShipHubApplication : Application(){
    private val debugTree = object : Timber.DebugTree() {
        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?
        ) = super.log(priority, "TAG", message, t)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initTimber()
    }

    private fun initTimber() = Timber.plant(debugTree)

    companion object {
        lateinit var instance: ShipHubApplication
            private set
    }
}
