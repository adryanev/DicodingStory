package dev.adryanev.dicodingstory.core.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.adryanev.dicodingstory.core.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @Before
    open fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    protected fun launchTest(block: suspend TestScope.() -> Unit) =
        testCoroutineRule
            .testCoroutineScope
            .launch(testCoroutineRule.testCoroutineDispatcher) { block }

}