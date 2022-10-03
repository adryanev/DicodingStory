package dev.adryanev.dicodingstory.features.story.presentation.story_detail.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionInflater
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.databinding.FragmentStoryDetailBinding
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class StoryDetailFragment : Fragment() {

    private var _binding: FragmentStoryDetailBinding? = null

    private val binding: FragmentStoryDetailBinding
        get() = _binding ?: throw UninitializedPropertyAccessException()

    private val args: StoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.detailStoryToolbar.toolbar.setupWithNavController(
            navController, appBarConfiguration
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSharedElementTransitionOnEnter()
        postponeEnterTransition()
        initView()
    }

    private fun setSharedElementTransitionOnEnter() {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

    private fun initView() {
        val story = args.story

        binding.apply {
            detailStoryAuthor.text = story.name
            detailStoryDescription.text = story.description
            detailStoryCoordinates.text = root.context.getString(
                R.string.coordinates, story.location?.latitude, story.location?.longitude
            )
            if (story.location == null) {
                detailStoryCoordinates.visibility = View.INVISIBLE
            }
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")
            val formattedDate = story.createdAt.format(formatter)

            detailStoryDate.text = formattedDate
            Picasso.get().load(story.photoUrl).fit().noFade().centerCrop()
                .into(detailStoryPhoto, object : Callback {
                    override fun onSuccess() {
                        startPostponedEnterTransition()

                    }

                    override fun onError(e: Exception?) {
                        startPostponedEnterTransition()

                    }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


