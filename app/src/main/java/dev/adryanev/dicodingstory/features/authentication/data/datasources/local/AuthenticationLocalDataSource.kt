package dev.adryanev.dicodingstory.features.authentication.data.datasources.local

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResult

interface AuthenticationLocalDataSource {

    suspend fun saveLoginData(loginResult: LoginResult): Either<Failure, Unit>
    suspend fun removeLoginData(): Either<Failure, Unit>
    suspend fun getLoginData(): Either<Failure, LoginResult?>

}