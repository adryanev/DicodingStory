package dev.adryanev.dicodingstory.features.authentication.presentation.login.viewmodels

import arrow.core.getOrElse
import dev.adryanev.dicodingstory.core.createLoginFailed
import dev.adryanev.dicodingstory.core.createLoginSuccess
import dev.adryanev.dicodingstory.core.createUser
import dev.adryanev.dicodingstory.core.presentation.viewmodels.BaseViewModelTest
import dev.adryanev.dicodingstory.core.utils.getOrAwaitValue
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.features.authentication.domain.entities.LoginForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.LogInUser
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.LogInUserParams
import dev.adryanev.dicodingstory.shared.domain.value_object.EmailAddress
import dev.adryanev.dicodingstory.shared.domain.value_object.Password
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class LoginFormViewModelTest : BaseViewModelTest() {

    private lateinit var systemUnderTest: LoginFormViewModel
    private val loggedInUser = mock<LogInUser>()
    private lateinit var user: User

    @Before
    fun setUp() {
        systemUnderTest = LoginFormViewModel(loggedInUser)
        user = createUser()
    }

    @Test
    fun `should set emailAddress state when called`() {
        testCoroutineRule.runTest {
            launchTest {
                val email = "rywukafe@getnada.com"
                systemUnderTest.emailAddressChanged(email)

                val state = systemUnderTest.state.getOrAwaitValue()
                assertEquals(state.emailAddress, EmailAddress(email))
            }
        }
    }

    @Test
    fun `should set password state when called`() {
        testCoroutineRule.runTest {
            launchTest {
                val password = "12345678"
                systemUnderTest.passwordChanged(password)

                val state = systemUnderTest.state.getOrAwaitValue()
                assertEquals(state.password, Password(password))
            }
        }
    }

    @Test
    fun `should return user when login success`() {
        testCoroutineRule.runTest {
            val email = "rywukafe@getnada.com"
            val password = "12345678"
            val loginForm = LoginForm(
                EmailAddress(email),
                Password(password)
            )
            Mockito.`when`(
                loggedInUser(
                    LogInUserParams(
                        loginForm
                    )
                )
            ).thenReturn(createLoginSuccess())
            launchTest {

                systemUnderTest.emailAddressChanged(email)
                systemUnderTest.passwordChanged(password)

                systemUnderTest.loginButtonPressed()

                Mockito.verify(loggedInUser).invoke(LogInUserParams(loginForm))
                val state = systemUnderTest.state.getOrAwaitValue()

                val either = state.loginResult.getOrElse { null }
                val data = either?.getOrElse { null }

                assertFalse(state.isLoading)
                assertEquals(data, user)
            }
        }
    }

    @Test
    fun `should return failure when login failed`() {
        testCoroutineRule.runTest {
            val email = "rywukafe@getnada.com"
            val password = "12345678"
            val loginForm = LoginForm(
                EmailAddress(email),
                Password(password)
            )
            Mockito.`when`(
                loggedInUser(
                    LogInUserParams(
                        loginForm
                    )
                )
            ).thenReturn(createLoginFailed())
            launchTest {

                systemUnderTest.emailAddressChanged(email)
                systemUnderTest.passwordChanged(password)

                systemUnderTest.loginButtonPressed()
                Mockito.verify(loggedInUser).invoke(LogInUserParams(loginForm))

                val state = systemUnderTest.state.getOrAwaitValue()

                val either = state.loginResult.getOrElse { null }
                val data = either?.getOrElse { null }

                assertFalse(state.isLoading)
                assertNull(data)
            }
        }
    }
}