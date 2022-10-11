package dev.adryanev.dicodingstory.features.story.presentation.story_list.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.core.presentations.flowRefresh
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.core.presentations.setSingleClick
import dev.adryanev.dicodingstory.databinding.FragmentStoryListBinding
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.features.story.presentation.story_home.pages.StoryHomeFragmentDirections
import dev.adryanev.dicodingstory.features.story.presentation.story_list.pages.adapters.StoryListAdapter
import dev.adryanev.dicodingstory.features.story.presentation.story_list.viewmodels.StoryListState
import dev.adryanev.dicodingstory.features.story.presentation.story_list.viewmodels.StoryListViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A fragment representing a list of Story.
 */
@AndroidEntryPoint
class StoryFragment : Fragment(), MviView<StoryListState> {

    private val viewModel: StoryListViewModel by viewModels()
    private var _binding: FragmentStoryListBinding? = null
    private val binding: FragmentStoryListBinding
        get() = _binding ?: throw UninitializedPropertyAccessException()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryListBinding.inflate(inflater, container, false)
        binding.storyFabAddStory.setSingleClick {
            navigateToCreateNewStory()
        }

        return binding.root
    }

    private fun navigateToCreateNewStory() {
        findNavController().navigate(StoryHomeFragmentDirections.actionStoryHomeFragmentToNewStoryFragment())
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

        val adapter = binding.storyRecyclerView.adapter as StoryListAdapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.storyRecyclerView.smoothScrollToPosition(0)

                }
            }
        })


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect {
                    binding.storyAppendProgress.isVisible = it.source.append is LoadState.Loading
                    binding.storyPrependProgress.isVisible = it.source.append is LoadState.Loading
                  
                }
            }
        }


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
        view?.findNavController()
        findNavController().navigate(
            StoryHomeFragmentDirections.actionStoryHomeFragmentToStoryDetailFragment(
                story
            ), extra
        )
    }

    override fun render(state: StoryListState) {
        Timber.d("New State : $state")
        with(state) {
            binding.storySwipeRefreshLayout.isRefreshing = isRefresh

            storyList.fold({}, { pagingData ->
                Timber.i("Story fetched Successfully: $pagingData")
                val adapter = binding.storyRecyclerView.adapter as StoryListAdapter
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        adapter.submitData(pagingData)

                    }
                }

            })

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}