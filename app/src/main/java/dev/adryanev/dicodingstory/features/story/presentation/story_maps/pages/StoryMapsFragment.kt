package dev.adryanev.dicodingstory.features.story.presentation.story_maps.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.presentations.error_handler.handleError
import dev.adryanev.dicodingstory.core.presentations.mvi.MviView
import dev.adryanev.dicodingstory.databinding.FragmentStoryMapsBinding
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.features.story.presentation.story_maps.viewmodels.StoryMapsState
import dev.adryanev.dicodingstory.features.story.presentation.story_maps.viewmodels.StoryMapsViewModel
import timber.log.Timber

@AndroidEntryPoint
class StoryMapsFragment : Fragment(), MviView<StoryMapsState> {

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        addStoryMarker(googleMap)

    }

    private val boundsBuilder = LatLngBounds.Builder()


    private fun addStoryMarker(googleMap: GoogleMap) {

        Timber.d("Story List: $storyList")
        if (storyList.isNotEmpty()) {
            storyList.forEach {
                if(it.location?.latitude != null){
                    val latLng = LatLng(
                        it.location.latitude, it.location.longitude
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(latLng)
                            .title(it.name)
                            .snippet(it.description)
                    )

                    boundsBuilder.include(latLng)
                }



            }
            val bounds: LatLngBounds = boundsBuilder.build()
            Timber.d("camera bounds: $bounds")
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }


    }


    private var _binding: FragmentStoryMapsBinding? = null
    private val binding: FragmentStoryMapsBinding
        get() = _binding!!
    private val viewModel: StoryMapsViewModel by viewModels()

    private var storyList: List<Story> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
    }

    private fun collectUiState() {
        viewModel.state.distinctUntilChanged().observe(
            viewLifecycleOwner, Observer(::render)
        )
    }

    private fun subscribeUi() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun render(state: StoryMapsState) {
        with(state) {
            storyList.fold({}, { either ->
                either.fold(
                    { failure -> requireContext().handleError(failure) }, ::updateMarker
                )
            })
        }
    }

    private fun updateMarker(stories: List<Story>) {

        storyList = stories
        subscribeUi()


    }
}