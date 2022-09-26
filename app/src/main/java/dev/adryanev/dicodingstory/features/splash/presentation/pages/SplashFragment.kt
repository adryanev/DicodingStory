package dev.adryanev.dicodingstory.features.splash.presentation.pages

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import dev.adryanev.dicodingstory.databinding.FragmentSplashBinding
import dev.adryanev.dicodingstory.features.splash.presentation.viewmodels.SplashViewModel
import dev.adryanev.dicodingstory.features.splash.presentation.viewmodels.SplashViewState
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : Fragment(), MviView<SplashViewState> {

    private val viewModel: SplashViewModel by viewModels()

    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkIsLoggedIn()
        collectUiState()

    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }

    override fun render(state: SplashViewState) {
        Timber.d("render method called")
        state.checkLoginOrFailure.fold(
            {
                Timber.d("none called")
            },
            { either ->
                either.fold(
                    { failure ->
                        Timber.d("Failure occured")
                        requireContext().handleError(failure)
                    },
                    { user ->
                        Timber.d("success fetch shared preferences")
                        Handler(Looper.getMainLooper()).postDelayed({
                            if (user != null) {
                                navigateToStory()
                            } else {
                                navigateToLogin()
                            }
                        }, 2000)

                    }
                )
            }
        )
    }


    private fun navigateToLogin() {
        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        navigate(action)
    }

    private fun navigateToStory() {
        val action = SplashFragmentDirections.actionSplashFragmentToStoryFragment()
        navigate(action)
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

}