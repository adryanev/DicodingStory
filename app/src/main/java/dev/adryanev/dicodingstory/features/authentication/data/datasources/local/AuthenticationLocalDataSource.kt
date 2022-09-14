package dev.adryanev.dicodingstory.features.authentication.data.datasources.local

import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResult
import dev.adryanev.functional_programming.Either
import dev.adryanev.functional_programming.Failure

interface AuthenticationLocalDataSource {

    suspend fun saveLoginData(loginResult: LoginResult): Either<Failure, Unit>
    suspend fun removeLoginData(): Either<Failure, Unit>
    suspend fun getLoginData(): Either<Failure, LoginResult?>

}