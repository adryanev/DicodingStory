package dev.adryanev.dicodingstory.features.story.presentation.story_list.pages

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
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
                    else -> false
                }

            }
        }

        return binding.root
    }

    private fun logoutUser() {

        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle(getString(R.string.logout))
            setMessage(
                getString(R.string.logout_message)
            )
            setPositiveButton(R.string.yes) { dialog, _ ->
                viewModel.logout()
                dialog.dismiss()

            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()

            }
            show()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
    }

    private fun collectUiState() {

        viewModel.state.distinctUntilChanged().observe(
            viewLifecycleOwner, Observer(::render)
        )
    }

    private fun initView() {
        binding.storyRecyclerView.adapter = StoryListAdapter(::navigateToStoryDetail)
        binding.storySwipeRefreshLayout.flowRefresh().map {
            viewModel.refreshPage()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        postponeEnterTransition()
        requireView().viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true

        }
    }

    private fun navigateToStoryDetail(sharedElement: Map<View, String>, story: Story) {
        val extra = FragmentNavigatorExtras(
            *sharedElement.map { it.key to it.value }.toTypedArray()
        )
        findNavController().navigate(
            StoryFragmentDirections.actionStoryFragmentToStoryDetailFragment(
                story
            ), extra
        )
    }

    override fun render(state: StoryListState) {
        Timber.d("New State : $state")
        with(state) {
            binding.storySwipeRefreshLayout.isRefreshing = isRefresh


            storyList.fold({}, { either ->
                either.fold({ failure ->
                    requireContext().handleError(failure)
                }, { stories ->
                    val currentRefresh = isRefresh
                    Timber.i("Story fetched Successfully: $stories")
                    if (!isLoading) {
                        (binding.storyRecyclerView.adapter as StoryListAdapter).submitList(stories) {
                            Timber.d("currentRefresh: $currentRefresh")
                            if (currentRefresh) {
                                binding.storyRecyclerView.smoothScrollToPosition(0)

                            }
                        }
                    }


                })
            })
            logout.fold({}, { either ->
                either.fold({ failure -> requireContext().handleError(failure) }, {
                    Timber.i("User logged out successfully")
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