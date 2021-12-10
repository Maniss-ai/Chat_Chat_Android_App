package com.example.splashscreen.models

import android.app.Application
import com.onesignal.OneSignal

const val ONESIGNAL_APP_ID = "321c17d0-13d9-488e-a152-822c904ee18f"

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}