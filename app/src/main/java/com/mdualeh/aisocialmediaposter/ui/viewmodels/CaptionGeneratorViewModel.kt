package com.mdualeh.aisocialmediaposter.ui.viewmodels

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdualeh.aisocialmediaposter.domain.repository.ImageDetectorRepository
import com.mdualeh.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.mdualeh.aisocialmediaposter.domain.util.Resource
import com.mdualeh.aisocialmediaposter.ui.state.GeneratorScreenState
import com.mdualeh.aisocialmediaposter.ui.model.SocialMediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CaptionGeneratorViewModel @Inject constructor(
    private val textCompletionRepository: TextCompletionRepository,
    private val imageDetectorRepository: ImageDetectorRepository,
) : ViewModel() {

    var state by mutableStateOf(GeneratorScreenState())
        private set

    // delete this later
    fun testGeneratorApi() {
        state = state.copy(
            isLoading = true,
            error = null
        )
        viewModelScope.launch {
            when (
                val result = textCompletionRepository.getReplyFromTextCompletionAPI(
                    keywords = state.loadedTags,
                    maxWords = state.selectedSocialMedia.maxTokenLimit,
                    type = state.selectedSocialMedia,
                )
            ) {
                is Resource.Success -> {
                    state = state.copy(
                        textCompletion = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        textCompletion = null,
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun processBitmap(resizedBitmap: Bitmap) {
        state = state.copy(
            image = resizedBitmap,
            isLoadingTags = true,
            error = null
        )
        viewModelScope.launch {
            when (val result = imageDetectorRepository.getTagsFromImage(resizedBitmap)) {
                is Resource.Success -> {
                    result.data?.let {
                        state.loadedTags.addAll(it)
                        state = state.copy(
                            isLoadingTags = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    state.loadedTags.clear()
                    state = state.copy(
                        isLoadingTags = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun clearBitmap() {
        state.loadedTags.clear()
        state = state.copy(
            image = null,
            textCompletion = null,
        )
    }

    fun addTag(tag: String) {
        state.loadedTags.add(tag)
    }

    fun removeTag(tag: String) {
        state.loadedTags.remove(tag)
    }

    fun clearGeneratedText() {
        state = state.copy(
            textCompletion = null
        )
    }

    fun updateModifiedText(text: String) {
        state.modifiedText = text
    }

    fun resetEverything() {
        state = GeneratorScreenState()
    }

    fun updateSelectedSocialMedia(socialMediaItem: SocialMediaItem) {
        state = state.copy(
            selectedSocialMedia = socialMediaItem.type,
            selectedSocialMediaItem = socialMediaItem,
        )
    }
}
