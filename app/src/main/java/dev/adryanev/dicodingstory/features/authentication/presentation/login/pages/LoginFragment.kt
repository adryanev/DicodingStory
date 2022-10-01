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
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.error_handler.handleError
import dev.adryanev.dicodingstory.core.presentations.error_handler.showToast
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.databinding.FragmentLoginBinding
import dev.adryanev.dicodingstory.features.authentication.presentation.login.viewmodels.LoginFormViewModel
import dev.adryanev.dicodingstory.features.authentication.presentation.login.viewmodels.LoginFormViewState
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(), MviView<LoginFormViewState> {

    private val viewModel: LoginFormViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.apply {
            loginEmailInput.setOnTextChangeCallback {
                Timber.d("Email: $it")
                if (it.isEmpty()) return@setOnTextChangeCallback
                viewModel.emailAddressChanged(it)
            }
            loginPasswordInput.setOnTextChangedCallback {
                Timber.d("Password: $it")
                if (it.isEmpty() || it.length < 6) return@setOnTextChangedCallback
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
            viewModel.state.collectLatest(::render)
        }
    }

    override fun render(state: LoginFormViewState) {
        if (state.isLoading) {
            val spec = CircularProgressIndicatorSpec(
                requireContext(),  /*attrs=*/
                null, 0, R.style.Theme_DicodingStory_CircularProgressIndicator_ExtraSmall_White
            )
            val progressIndicatorDrawable =
                IndeterminateDrawable.createCircularDrawable(requireContext(), spec)
            binding.loginLoginButton.apply {
                icon = progressIndicatorDrawable
                isClickable = false
                isEnabled = false
            }

        } else {
            binding.loginLoginButton.apply {
                icon = null
                isClickable = false
                isEnabled = false
            }
        }
        state.loginResult.fold({}, { either ->
            either.fold({ failure ->
                Timber.i("Failure occurred: $failure")
                requireContext().handleError(failure)
            }, { user ->
                Timber.i("Success Logging in User: ${user?.name}")
                if (user != null) {
                    requireContext().showToast(getString(R.string.welcome, user.name))
                    navigate(LoginFragmentDirections.actionLoginFragmentToStoryFragment())
                }
            })
        })
    }

    private fun navigate(action: NavDirections) {
        Timber.i("Navigate to ${action.actionId}")
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}