package dev.adryanev.dicodingstory.features.authentication.data.datasources.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import dev.adryanev.dicodingstory.core.domain.failures.SharedPreferenceFailure
import dev.adryanev.dicodingstory.core.utils.PreferenceConstants
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResult
import dev.adryanev.functional_programming.Either
import dev.adryanev.functional_programming.Failure
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

            Either.Success(Unit)
        } catch (exception: Exception) {
            Either.Error(SharedPreferenceFailure("Cannot save data to shared preference"))
        }

    }

    override suspend fun removeLoginData(): Either<Failure, Unit> {
        return try {
            sharedPreferences.edit {
                clear()
            }
            Either.Success(Unit)
        } catch (exception: Exception) {
            Either.Error(SharedPreferenceFailure("Cannot clear Shared Preference data"))
        }
    }

    override suspend fun getLoginData(): Either<Failure, LoginResult?> {
        return try {
            val adapter = moshi.adapter(LoginResult::class.java)
            val data = sharedPreferences.getString(PreferenceConstants.USER, "")
            val user = adapter.fromJson(data)
            Either.Success(user)

        } catch (exception: Exception) {
            Either.Error(SharedPreferenceFailure("Cannot get data from sharedPreference"))
        }
    }
}