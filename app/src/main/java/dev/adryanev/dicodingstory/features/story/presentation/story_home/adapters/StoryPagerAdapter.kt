package dev.adryanev.dicodingstory.features.story.presentation.story_home.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.features.story.presentation.story_list.pages.StoryFragment
import dev.adryanev.dicodingstory.features.story.presentation.story_maps.pages.StoryMapsFragment

class StoryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(R.string.list, R.string.map)

        const val NUM_PAGES = 2
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> StoryFragment()
            1 -> StoryMapsFragment()
            else -> Fragment()
        }

}