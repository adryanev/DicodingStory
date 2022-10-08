package dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import arrow.core.none
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewModel
import dev.adryanev.dicodingstory.core.utils.resource.ResourceProvider
import dev.adryanev.dicodingstory.core.utils.toOneTimeEvent
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.GetLoggedInUser
import dev.adryanev.dicodingstory.features.story.domain.entities.StoryForm
import dev.adryanev.dicodingstory.features.story.domain.usecases.CreateNewStory
import dev.adryanev.dicodingstory.features.story.domain.usecases.CreateNewStoryParams
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import dev.adryanev.dicodingstory.services.locations.domain.usecases.GetCurrentLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewStoryViewModel @Inject constructor(
    private val getLoggedInUser: GetLoggedInUser,
    private val createNewStory: CreateNewStory,
    private val resourceProvider: ResourceProvider,
    private val getCurrentLocation: GetCurrentLocation,
) : ViewModel(), MviViewModel<NewStoryState> {

    private val _state = MutableStateFlow(NewStoryState.initial())
    override val state: LiveData<NewStoryState>
        get() = _state.asLiveData()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            getLoggedInUser(NoParams).collectLatest { either ->
                _state.update {
                    it.copy(getLoggedInUser = Option.fromNullable(either))
                }
                _state.update {
                    it.copy(isLoading = false, getLoggedInUser = none())
                }

            }
        }
    }

    fun setUser(user: User?) {
        _state.update {
            it.copy(loggedInUser = user)
        }
    }

    fun descriptionChanged(description: String) {
        _state.update {
            it.copy(description = description)
        }

    }

    fun setStroyPicture(
        picture: File
    ) {
        _state.update {
            it.copy(
                storyPicture = picture
            )
        }
    }

    fun uploadStory() {
        val picture = _state.value.storyPicture
        val description = _state.value.description
        val location = _state.value.userLocation

        if (picture == null) {
            _state.update {
                it.copy(
                    errorMessage = resourceProvider.getString(R.string.empty_image).toOneTimeEvent()
                )
            }
            _state.update {
                it.copy(
                    errorMessage = null
                )
            }
            return
        }
        if (description.isNullOrEmpty()) {
            _state.update {
                it.copy(
                    errorMessage = resourceProvider.getString(R.string.empty_description)
                        .toOneTimeEvent()
                )
            }
            _state.update {
                it.copy(
                    errorMessage = null
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            createNewStory(
                CreateNewStoryParams(
                    StoryForm(
                        description = description,
                        location = location,
                        photo = picture
                    )
                )
            ).collectLatest { either ->
                _state.update {
                    it.copy(
                        createNewStory = Option.fromNullable(either)
                    )
                }
                _state.update {
                    it.copy(isLoading = false, createNewStory = none())
                }
            }
        }

    }

    fun reset() {
        _state.update {
            NewStoryState.initial()
        }
    }

    fun getUserLocation(){
        viewModelScope.launch {
            getCurrentLocation(NoParams).collectLatest { either ->
                _state.update {
                    it.copy(getUserLocation = Option.fromNullable(either))
                }
            }
        }
    }

    fun setUserLocation(location: Location){
        _state.update { it.copy(userLocation = location) }
    }


}