package dev.adryanev.dicodingstory.features.authentication.data.datasources.local

import android.content.SharedPreferences
import androidx.core.content.edit
import arrow.core.Either
import arrow.core.right
import com.squareup.moshi.Moshi
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.failures.SharedPreferenceFailure
import dev.adryanev.dicodingstory.core.utils.PreferenceConstants
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResult

import javax.inject.Inject

class AuthenticationLocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val moshi: Moshi,
) : AuthenticationLocalDataSource {
    override suspend fun saveLoginData(loginResult: LoginResult): Either<Failure, Unit> {
        return try {
            val adapter = moshi.adapter(LoginResult::class.java)
            val data = adapter.toJson(loginResult)
            sharedPreferences.edit {
                putString(
                    PreferenceConstants.USER,
                    data
                )
                apply()
            }

            Unit.right()
        } catch (exception: Exception) {
            Either.Left(SharedPreferenceFailure("Cannot save data to shared preference"))
        }

    }

    override suspend fun removeLoginData(): Either<Failure, Unit> {
        return try {
            sharedPreferences.edit {
                clear()
            }
            Either.Right(Unit)
        } catch (exception: Exception) {
            Either.Left(SharedPreferenceFailure("Cannot clear Shared Preference data"))
        }
    }

    override suspend fun getLoginData(): Either<Failure, LoginResult?> {
        return try {
            val adapter = moshi.adapter(LoginResult::class.java)
            val data = sharedPreferences.getString(PreferenceConstants.USER, "")
            val user = data?.let { adapter.fromJson(it) }
            Either.Right(user)

        } catch (exception: Exception) {
            Either.Left(SharedPreferenceFailure("Cannot get data from sharedPreference"))
        }
    }
}