package dev.adryanev.dicodingstory.features.story.presentation.new_story.pages

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.error_handler.handleError
import dev.adryanev.dicodingstory.core.presentations.error_handler.showToast
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.core.presentations.setSingleClick
import dev.adryanev.dicodingstory.core.utils.consumeOnce
import dev.adryanev.dicodingstory.core.utils.uriToFile
import dev.adryanev.dicodingstory.databinding.FragmentNewStoryBinding
import dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels.NewStoryState
import dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels.NewStoryViewModel
import timber.log.Timber

@AndroidEntryPoint
class NewStoryFragment : Fragment(), MviView<NewStoryState> {

    private var _binding: FragmentNewStoryBinding? = null
    private val binding: FragmentNewStoryBinding
        get() = _binding ?: throw UninitializedPropertyAccessException()

    private val viewModel: NewStoryViewModel by activityViewModels()

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg: Uri = result.data?.data as Uri
                val file = uriToFile(selectedImg, requireContext())

                viewModel.setStroyPicture(file)
            }
        }
    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                // Precise location access granted.
                getMyLastLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                // Only approximate location access granted.
                getMyLastLocation()
            }
            else -> {
                // No location access granted.
                requireContext().showToast("No Location Permission granted")
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewStoryBinding.inflate(inflater, container, false)
        binding.apply {
            newStoryDescriptionTextInputEditText.doAfterTextChanged { text ->
                Timber.d("New Text: $text")
                if (text.isNullOrEmpty()) {
                    newStoryDescriptionTextInputLayout.error = getString(R.string.empty_description)
                } else {
                    newStoryDescriptionTextInputLayout.error = null

                }
                viewModel.descriptionChanged(text.toString())
            }
            newStoryCameraButton.setSingleClick {
                startTakePhoto()
            }
            newStoryGalleryButton.setSingleClick {
                startGallery()
            }
            newStoryUploadButton.setSingleClick {
                viewModel.uploadStory()
            }
            newStoryGetCoordinates.setSingleClick {
                getMyLastLocation()
            }
        }
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.newStoryToolbar.toolbar.setupWithNavController(navController, appBarConfiguration)
        Picasso.get().load(R.drawable.placeholder_upload).fit().centerCrop().noFade()
            .into(binding.newStoryPreviewImage)

        return binding.root
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            viewModel.getUserLocation()
        } else {
            requestLocationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        galleryLauncher.launch(chooser)
    }

    private fun startTakePhoto() {
        findNavController().navigate(NewStoryFragmentDirections.actionNewStoryFragmentToCameraPreviewFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
    }

    private fun collectUiState() {
        viewModel.state.observe(viewLifecycleOwner, Observer(::render))
    }

    override fun render(state: NewStoryState) {
        Timber.d("New State: $state")
        if (state.isLoading) {
            val spec = CircularProgressIndicatorSpec(
                requireContext(),  /*attrs=*/
                null, 0, R.style.Theme_DicodingStory_CircularProgressIndicator_ExtraSmall_White
            )
            val progressIndicatorDrawable =
                IndeterminateDrawable.createCircularDrawable(requireContext(), spec)
            binding.newStoryUploadButton.apply {
                icon = progressIndicatorDrawable
                isClickable = false
                isEnabled = false
            }
        } else {
            if (state.description.isNullOrEmpty() && state.storyPicture == null) {
                binding.newStoryUploadButton.apply {
                    icon = null
                    isClickable = false
                    isEnabled = false
                }
            } else {
                binding.newStoryUploadButton.apply {
                    icon = null
                    isClickable = true
                    isEnabled = true
                }
            }
        }

        state.getLoggedInUser.fold({}, { either ->
            either.fold({ failure ->
                requireContext().handleError(failure)
            }, { user ->
                Timber.i("Success get current user: $user")
                viewModel.setUser(user)
            })
        })

        state.errorMessage?.consumeOnce {
            requireContext().showToast(it)
        }

        if (state.storyPicture != null) {
            Picasso.get().load(state.storyPicture).fit().centerCrop()
                .into(binding.newStoryPreviewImage)
        }

        state.createNewStory.fold({}, { either ->
            either.fold({ failure -> requireContext().handleError(failure) }, {
                requireContext().showToast(getString(R.string.create_story_success))
                navigateToStoryList()
            })
        })

        state.getUserLocation.fold({}, { either ->
            either.fold({ failure -> requireContext().handleError(failure) }, {
                viewModel.setUserLocation(it)
            })
        })

        if (state.userLocation != null) {
            binding.newStoryCoordinates.text = getString(
                R.string.coordinates, state.userLocation.latitude, state.userLocation.longitude
            )
        }
    }

    private fun navigateToStoryList() {
        findNavController().navigate(NewStoryFragmentDirections.actionNewStoryFragmentToStoryHomeFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.reset()
    }
}