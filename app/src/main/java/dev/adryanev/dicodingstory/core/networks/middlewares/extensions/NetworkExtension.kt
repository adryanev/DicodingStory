package dev.adryanev.dicodingstory.core.networks.middlewares.extensions

//import dev.adryanev.functional_programming.Either
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.squareup.moshi.JsonAdapter
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.failures.NetworkFailure
import dev.adryanev.dicodingstory.core.domain.failures.SslFailure
import dev.adryanev.dicodingstory.core.domain.failures.TimeOutFailure
import dev.adryanev.dicodingstory.core.networks.middlewares.NetworkMiddleware
import dev.adryanev.dicodingstory.core.networks.models.ErrorResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.BufferedSource
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException

/**
 * @param middlewares list of customizable [NetworkMiddleware] that would returns its error
 *  if one of them is not valid.
 * @param ioDispatcher the [CoroutineDispatcher] for safe calling.
 * @param adapter the adapter for error response.
 * @param retrofitCall retrofit invocation block.
 */
suspend inline fun <T> safeCall(
    middlewares: List<NetworkMiddleware> = emptyList(),
    ioDispatcher: CoroutineDispatcher,
    adapter: JsonAdapter<ErrorResponse>,
    crossinline retrofitCall: suspend () -> T
): Either<Failure, T> {
    return runMiddlewares(middlewares = middlewares)?.left()
        ?: executeRetrofitCall(ioDispatcher, adapter, retrofitCall)
}

/**
 * Iterate all the [NetworkMiddleware] and return true if all of them are valid.
 * @return []
 */
fun runMiddlewares(
    middlewares: List<NetworkMiddleware> = emptyList(),
): Failure? {
    if (middlewares.isEmpty()) return null
    return middlewares.find { !it.isValid() }?.failure
}

suspend inline fun <T> executeRetrofitCall(
    ioDispatcher: CoroutineDispatcher,
    adapter: JsonAdapter<ErrorResponse>,
    crossinline retrofitCall: suspend () -> T
): Either<Failure, T> {
    return withContext(ioDispatcher) {
        try {
            return@withContext retrofitCall().right()
        } catch (e: Exception) {
            return@withContext e.parseException(adapter).left()
        }
    }
}

/**
 * @param adapter for parsing error message
 */
fun Throwable.parseException(
    adapter: JsonAdapter<ErrorResponse>
): Failure {
    return when (this) {
        is SocketTimeoutException -> TimeOutFailure
        is SSLException -> SslFailure
        is HttpException -> {
            val errorService = adapter.parseError(response()?.errorBody()?.source())
            if (errorService != null) {
                NetworkFailure(
                    code = this.code(),
                    message = errorService.message ?: ""
                )
            } else {
                Failure.UnexpectedFailure(
                    message = "Service ERROR BODY does not match."
                )
            }
        }
        else -> Failure.UnexpectedFailure(
            message = message ?: "Exception not handled caused an Unknown failure"
        )
    }
}

private fun JsonAdapter<ErrorResponse>.parseError(
    json: BufferedSource?
): ErrorResponse? {
    return if (json != null) {
        fromJson(json)
    } else {
        null
    }
}