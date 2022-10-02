package dev.adryanev.dicodingstory.features.authentication.presentation.register.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.error_handler.handleError
import dev.adryanev.dicodingstory.core.presentations.error_handler.showToast
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.core.presentations.setSingleClick
import dev.adryanev.dicodingstory.databinding.FragmentRegisterBinding
import dev.adryanev.dicodingstory.features.authentication.presentation.register.viewmodels.RegisterFormViewModel
import dev.adryanev.dicodingstory.features.authentication.presentation.register.viewmodels.RegisterFormViewState
import timber.log.Timber

@AndroidEntryPoint
class RegisterFragment : Fragment(), MviView<RegisterFormViewState> {
    private val viewModel: RegisterFormViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.apply {
            registerEmailInput.setOnTextChangeCallback {
                Timber.d("Email: $it")
                if (it.isEmpty()) return@setOnTextChangeCallback
                viewModel.emailAddressChanged(it)
            }
            registerPasswordInput.setOnTextChangedCallback {
                Timber.d("Password: $it")
                if (it.isEmpty() || it.length < 6) return@setOnTextChangedCallback
                viewModel.passwordChanged(it)
            }
            registerNameInput.doAfterTextChanged { text ->
                if (text.isNullOrEmpty()) {
                    registerNameInput.error = requireContext().getString(R.string.empty_name)
                    return@doAfterTextChanged
                }
                viewModel.nameChanged(text.toString())
            }
            registerButton.setSingleClick {
                viewModel.registerButtonPressed()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.registerToolbar.setupWithNavController(navController, appBarConfiguration)
        collectUiState()
    }

    private fun collectUiState() {
        viewModel.state.observe(viewLifecycleOwner, Observer(::render))
    }

    override fun render(state: RegisterFormViewState) {
        if (state.isLoading) {
            val spec = CircularProgressIndicatorSpec(
                requireContext(),  /*attrs=*/
                null, 0, R.style.Theme_DicodingStory_CircularProgressIndicator_ExtraSmall_White
            )
            val progressIndicatorDrawable =
                IndeterminateDrawable.createCircularDrawable(requireContext(), spec)
            binding.registerButton.apply {
                icon = progressIndicatorDrawable
                isClickable = false
                isEnabled = false
            }
        } else {
            if (!state.name.isNullOrEmpty() && state.emailAddress != null && state.password != null) {
                binding.registerButton.apply {
                    icon = null
                    isClickable = true
                    isEnabled = true
                }
            } else {
                binding.registerButton.apply {
                    icon = null
                    isClickable = false
                    isEnabled = false
                }
            }
        }
        state.registerResult.fold({}, { either ->
            either.fold({ failure ->
                Timber.i("failure occured: $failure")
                requireContext().handleError(failure)
            }, {
                requireContext().showToast(getString(R.string.register_success))
                navigateToLogin()

            })
        })
    }

    private fun navigateToLogin() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}