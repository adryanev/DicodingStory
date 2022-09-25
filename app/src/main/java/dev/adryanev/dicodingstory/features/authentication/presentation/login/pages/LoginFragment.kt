package dev.adryanev.dicodingstory.features.authentication.presentation.login.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.features.authentication.presentation.login.viewmodels.LoginFormViewModel
import dev.adryanev.dicodingstory.features.authentication.presentation.login.viewmodels.LoginFormViewState

@AndroidEntryPoint
class LoginFragment : Fragment(),MviView<LoginFormViewState> {

    private val viewModel: LoginFormViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun render(state: LoginFormViewState) {
        TODO("Not yet implemented")
    }
}