package dev.adryanev.dicodingstory.core.presentations.error_handler

import android.content.Context
import android.widget.Toast
import dev.adryanev.dicodingstory.core.domain.failures.*

fun Context.handleError(failure: Failure) {
    when (failure) {
        is SslFailure -> showToast(failure.toString())
        is NetworkFailure -> showToast(failure.message)
        is NetworkMiddlewareFailure -> showToast(failure.message)
        is TimeOutFailure -> showToast(failure.toString())
        is DatabaseFailure -> showToast(failure.message)
        is SharedPreferenceFailure -> showToast(failure.message)
        is ServiceFailure -> showToast(failure.toString())
        is Failure.UnexpectedFailure -> showToast(failure.message)
        is Failure.CustomFailure -> showToast(failure.toString())
    }
}

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}