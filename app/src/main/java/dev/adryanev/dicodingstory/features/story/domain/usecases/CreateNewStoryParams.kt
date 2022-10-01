package dev.adryanev.dicodingstory.features.story.domain.usecases

import dev.adryanev.dicodingstory.features.story.domain.entities.StoryForm

data class CreateNewStoryParams(
    val storyForm: StoryForm
)