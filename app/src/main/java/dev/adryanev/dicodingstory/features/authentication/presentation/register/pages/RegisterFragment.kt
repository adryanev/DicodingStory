package dev.adryanev.dicodingstory.features.authentication.presentation.register.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.databinding.FragmentRegisterBinding
import dev.adryanev.dicodingstory.features.authentication.presentation.register.viewmodels.RegisterFormViewModel
import dev.adryanev.dicodingstory.features.authentication.presentation.register.viewmodels.RegisterFormViewState

@AndroidEntryPoint
class RegisterFragment : Fragment(), MviView<RegisterFormViewState> {
    private val viewModel: RegisterFormViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.apply {
            registerEmailInput.setOnTextChangeCallback {
                viewModel.emailAddressChanged(it)
            }
            registerPasswordInput.setOnTextChangedCallback {
                viewModel.passwordChanged(it)
            }
            registerNameInput.doAfterTextChanged { text ->
                if (text.isNullOrEmpty()) {
                    registerNameInput.error = requireContext().getString(R.string.empty_name)

                } else {
                    viewModel.nameChanged(text.toString())
                }

            }
            registerButton.setOnClickListener {
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
    }

    override fun render(state: RegisterFormViewState) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

        }
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

}