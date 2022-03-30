package com.example.fallingwords

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class FallingWordApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
    }
}
