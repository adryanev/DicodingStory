package dev.adryanev.dicodingstory.features.authentication.presentation.login.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.core.presentations.error_handler.handleError
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.databinding.FragmentLoginBinding
import dev.adryanev.dicodingstory.features.authentication.presentation.login.viewmodels.LoginFormViewModel
import dev.adryanev.dicodingstory.features.authentication.presentation.login.viewmodels.LoginFormViewState
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(), MviView<LoginFormViewState> {

    private val viewModel: LoginFormViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.apply {
            loginEmailInput.setOnTextChangeCallback {
                Timber.d("Email: $it")
                viewModel.emailAddressChanged(it)
            }
            loginPasswordInput.setOnTextChangedCallback {
                Timber.d("Password: $it")
                viewModel.passwordChanged(it)
            }
            loginLoginButton.setOnClickListener {
                Timber.i("Login Button Pressed")
                viewModel.loginButtonPressed()
            }
            loginRegisterText.setOnClickListener {
                navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect(::render)
        }
    }

    override fun render(state: LoginFormViewState) {
        state.loginResult.fold(
            {},
            { either ->
                either.fold(
                    { failure ->
                        Timber.i("Failure occurred: $failure")
                        requireContext().handleError(failure)
                    },
                    { user ->
                        Timber.i("Success Logging in User: ${user?.name}")
                        if (user != null) {
                            navigate(LoginFragmentDirections.actionLoginFragmentToStoryFragment())
                        }
                    }
                )
            }
        )
    }

    private fun navigate(action: NavDirections) {
        Timber.i("Navigate to ${action.actionId}")
        findNavController().navigate(action)
    }
}