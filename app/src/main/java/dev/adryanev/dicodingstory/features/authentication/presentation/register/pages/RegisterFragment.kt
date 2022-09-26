package dev.adryanev.dicodingstory.features.authentication.presentation.register.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.features.authentication.presentation.register.viewmodels.RegisterFormViewState

@AndroidEntryPoint
class RegisterFragment : Fragment(), MviView<RegisterFormViewState> {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun render(state: RegisterFormViewState) {
        TODO("Not yet implemented")
    }

}