package dev.adryanev.dicodingstory.app

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import dev.adryanev.dicodingstory.BuildConfig
import dev.adryanev.dicodingstory.core.utils.TimberReleaseTree
import timber.log.Timber

@HiltAndroidApp
class StoryApplication : Application() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberReleaseTree())
        }

        Stetho.initializeWithDefaults(this)


        super.onCreate()
    }
}