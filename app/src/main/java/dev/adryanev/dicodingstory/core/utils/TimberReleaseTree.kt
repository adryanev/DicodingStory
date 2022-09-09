package dev.adryanev.dicodingstory.core.utils

import android.util.Log
import org.jetbrains.annotations.NotNull
import timber.log.Timber

class TimberReleaseTree : @NotNull Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//        if (priority == Log.ERROR || priority == Log.WARN) {
//            // Send report to crashlytics
//        }
    }
}