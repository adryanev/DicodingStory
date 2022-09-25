package dev.adryanev.dicodingstory.features.splash.presentation.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.features.splash.presentation.viewmodels.SplashViewModel
import dev.adryanev.dicodingstory.features.splash.presentation.viewmodels.SplashViewState

@AndroidEntryPoint
class SplashFragment : Fragment(), MviView<SplashViewState> {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun render(state: SplashViewState) {
    }
}