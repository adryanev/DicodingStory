package dev.adryanev.dicodingstory.features.story.presentation.new_story.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.core.presentations.error_handler.handleError
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.databinding.FragmentNewStoryBinding
import dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels.NewStoryState
import dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels.NewStoryViewModel

@AndroidEntryPoint
class NewStoryFragment : Fragment(), MviView<NewStoryState> {

    private var _binding: FragmentNewStoryBinding? = null
    private val binding: FragmentNewStoryBinding
        get() = _binding!!

    private val viewModel: NewStoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
    }

    private fun collectUiState() {
        viewModel.state.observe(viewLifecycleOwner, Observer(::render))
    }

    override fun render(state: NewStoryState) {


        state.getLoggedInUser.fold({}, { either ->
            either.fold({ failure ->
                requireContext().handleError(failure)
            }, { user ->
                viewModel.setUser(user)
            })
        })
    }
}