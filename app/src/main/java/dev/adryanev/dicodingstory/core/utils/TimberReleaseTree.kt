package dev.adryanev.dicodingstory.core.utils

import android.annotation.SuppressLint
import android.util.Log
import org.jetbrains.annotations.NotNull
import timber.log.Timber


class TimberReleaseTree : @NotNull Timber.Tree() {

    companion object {
        const val MAX_LOG_LENGTH = 4000
    }

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        if (isLoggable(tag, priority)) {
            // Message is short enough, doesn't need to be broken into chunks
            if (message.length < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, message)
                } else {
                    Log.println(priority, tag, message)
                }
                return
            }

            // Split by line, then ensure each line can fit into Log's max length
            var i = 0
            val length: Int = message.length
            while (i < length) {
                var newline = message.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = newline.coerceAtMost(i + MAX_LOG_LENGTH)
                    val part = message.substring(i, end)
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, part)
                    } else {
                        Log.println(priority, tag, part)
                    }
                    i = end
                } while (i < newline)
                i++
            }


        }
    }

}