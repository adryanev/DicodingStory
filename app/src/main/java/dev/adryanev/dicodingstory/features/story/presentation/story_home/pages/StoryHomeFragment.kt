package dev.adryanev.dicodingstory.features.story.presentation.story_home.pages

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.error_handler.handleError
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.databinding.FragmentStoryHomeBinding
import dev.adryanev.dicodingstory.features.story.presentation.story_home.adapters.StoryPagerAdapter
import dev.adryanev.dicodingstory.features.story.presentation.story_home.viewmodels.StoryHomeState
import dev.adryanev.dicodingstory.features.story.presentation.story_home.viewmodels.StoryHomeViewModel
import timber.log.Timber

@AndroidEntryPoint
class StoryHomeFragment : Fragment(), MviView<StoryHomeState> {

    private var _binding: FragmentStoryHomeBinding? = null
    private val binding: FragmentStoryHomeBinding
        get() = _binding!!

    private val viewModel: StoryHomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryHomeBinding.inflate(inflater, container, false)

        val navController = findNavController()
        val appBarConfiguration =
            AppBarConfiguration(topLevelDestinationIds = setOf(R.id.storyHomeFragment))
        binding.storyHomeViewPager.isUserInputEnabled = false
        binding.storyHomeToolbar.apply {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storyPagerAdapter = StoryPagerAdapter(this)
        binding.storyHomeViewPager.adapter = storyPagerAdapter

        TabLayoutMediator(binding.storyHomeTabLayout, binding.storyHomeViewPager) { tab, position ->
            tab.text = getString(StoryPagerAdapter.TAB_TITLES[position])
        }.attach()

        collectUiState()
    }

    private fun collectUiState() {
        viewModel.state.distinctUntilChanged().observe(
            viewLifecycleOwner, Observer(::render)
        )
    }

    private fun navigateToLogin() {
        Timber.d("Navigate to login")
        findNavController().navigate(
            StoryHomeFragmentDirections.actionStoryHomeFragmentToLoginFragment()
        )
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

    override fun render(state: StoryHomeState) {
        with(state) {
            logout.fold({}, { either ->
                either.fold({ failure -> requireContext().handleError(failure) }, {
                    Timber.i("User logged out successfully")
                    navigateToLogin()
                })
            })
        }

    }


}