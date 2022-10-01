package dev.adryanev.dicodingstory.features.story.presentation.story_list.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.error_handler.handleError
import dev.adryanev.dicodingstory.core.presentations.flowRefresh
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.databinding.FragmentStoryListBinding
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.features.story.presentation.story_list.pages.adapters.StoryListAdapter
import dev.adryanev.dicodingstory.features.story.presentation.story_list.viewmodels.StoryListState
import dev.adryanev.dicodingstory.features.story.presentation.story_list.viewmodels.StoryListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * A fragment representing a list of Story.
 */
@AndroidEntryPoint
class StoryFragment : Fragment(), MviView<StoryListState> {

    private val viewModel: StoryListViewModel by viewModels()
    private var _binding: FragmentStoryListBinding? = null
    private val binding: FragmentStoryListBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryListBinding.inflate(inflater, container, false)
        val navController = findNavController()
        val appBarConfiguration =
            AppBarConfiguration(topLevelDestinationIds = setOf(R.id.storyFragment))

        binding.storyToolbar.toolbar.apply {
            setupWithNavController(navController, appBarConfiguration)
            inflateMenu(R.menu.menu_story)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_item_logout -> {
                        logoutUser()
                        true

                    }
                    else ->
                        false
                }

            }
        }
        return binding.root
    }

    private fun logoutUser() {
        viewModel.logout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.getLatestStory()
            viewModel.state.collectLatest(::render)
        }
    }

    private fun initView() {
        binding.storyRecyclerView.adapter = StoryListAdapter {
            navigateToStoryDetail(it)
        }
        binding.storySwipeRefreshLayout.flowRefresh().map {
            viewModel.getLatestStory()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun navigateToStoryDetail(story: Story) {
        findNavController().navigate(
            StoryFragmentDirections.actionStoryFragmentToStoryDetailFragment(
                story
            )
        )
    }

    override fun render(state: StoryListState) {
        with(state) {
            binding.storySwipeRefreshLayout.isRefreshing = isLoading

            storyList.fold({}, { either ->
                either.fold({ failure ->
                    requireContext().handleError(failure)
                }, { stories ->
                    Timber.i("Story fetched Successfully: $stories")
                    (binding.storyRecyclerView.adapter as StoryListAdapter).submitList(stories)
                })
            })
            logout.fold({},
                { either ->
                    either.fold({ failure -> requireContext().handleError(failure) }, {
                        Timber.d("User logged out successfully")
                        navigateToLogin()
                    })
                })
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(StoryFragmentDirections.actionStoryFragmentToLoginFragment())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}