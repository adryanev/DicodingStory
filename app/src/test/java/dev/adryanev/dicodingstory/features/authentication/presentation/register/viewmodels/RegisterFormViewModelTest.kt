package dev.adryanev.dicodingstory.features.authentication.presentation.register.viewmodels

import arrow.core.getOrElse
import dev.adryanev.dicodingstory.core.createRegisterFailure
import dev.adryanev.dicodingstory.core.createRegisterSuccess
import dev.adryanev.dicodingstory.core.presentation.viewmodels.BaseViewModelTest
import dev.adryanev.dicodingstory.core.utils.getOrAwaitValue
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.features.authentication.domain.entities.RegisterForm
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.RegisterUser
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.RegisterUserParams
import dev.adryanev.dicodingstory.shared.domain.value_object.EmailAddress
import dev.adryanev.dicodingstory.shared.domain.value_object.Password
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.MockedConstruction.MockInitializer
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterFormViewModelTest : BaseViewModelTest() {

    private lateinit var systemUnderTest: RegisterFormViewModel
    private val registerUser = mock<RegisterUser>()

    @Before
    fun setUp() {
        systemUnderTest = RegisterFormViewModel(registerUser)
    }

    @Test
    fun emailAddressChanged() {
        testCoroutineRule.runTest {
            launchTest {
                val email = "rywukafe@getnada.com"
                systemUnderTest.emailAddressChanged(email)

                val state = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(state.emailAddress, EmailAddress(email))
            }
        }
    }

    @Test
    fun passwordChanged() {
        testCoroutineRule.runTest {
            launchTest {
                val password = "12345678"
                systemUnderTest.passwordChanged(password)

                val state = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(state.password, Password(password))
            }
        }
    }

    @Test
    fun nameChanged() {
        testCoroutineRule.runTest {
            launchTest {
                val name = "Adryan Eka Vandra"
                systemUnderTest.nameChanged(name)

                val state = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(state.name, name)
            }
        }
    }

    @Test
    fun registerButtonPressedSuccess() {
        testCoroutineRule.runTest {
            val email = "rywukafe@getnada.com"
            val password = "12345678"
            val name = "Adryan Eka Vandra"
            val registerForm = RegisterForm(
                name,
                EmailAddress(email),
                Password(password)
            )
            Mockito.`when`(
                registerUser(
                    RegisterUserParams(
                        registerForm
                    )
                )
            ).thenReturn(createRegisterSuccess())
            launchTest {

                systemUnderTest.emailAddressChanged(email)
                systemUnderTest.passwordChanged(password)
                systemUnderTest.nameChanged(name)

                systemUnderTest.registerButtonPressed()
                Mockito.verify(registerUser).invoke(RegisterUserParams(registerForm))

                val state = systemUnderTest.state.getOrAwaitValue()

                val either = state.registerResult.getOrElse { null }
                val data = either?.getOrElse { null }

                Assert.assertFalse(state.isLoading)
                Assert.assertTrue(data is Unit)
            }
        }
    }

    @Test
    fun registerButtonPressedFailed() {
        testCoroutineRule.runTest {
            val email = "rywukafe@getnada.com"
            val password = "12345678"
            val name = "Adryan Eka Vandra"
            val registerForm = RegisterForm(
                name,
                EmailAddress(email),
                Password(password)
            )
            Mockito.`when`(
                registerUser(
                    RegisterUserParams(
                        registerForm
                    )
                )
            ).thenReturn(createRegisterFailure())
            launchTest {

                systemUnderTest.emailAddressChanged(email)
                systemUnderTest.passwordChanged(password)
                systemUnderTest.nameChanged(name)

                systemUnderTest.registerButtonPressed()

                Mockito.verify(registerUser).invoke(RegisterUserParams(registerForm))

                val state = systemUnderTest.state.getOrAwaitValue()

                val either = state.registerResult.getOrElse { null }
                val data = either?.getOrElse { null }

                Assert.assertFalse(state.isLoading)
                Assert.assertNull(data)
            }
        }
    }
}