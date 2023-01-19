package com.devinjapan.aisocialmediaposter.ui.viewmodels

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devinjapan.aisocialmediaposter.data.repository.DataStoreRepositoryImpl
import com.devinjapan.aisocialmediaposter.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.domain.repository.ImageDetectorRepository
import com.devinjapan.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.devinjapan.aisocialmediaposter.domain.util.Resource
import com.devinjapan.aisocialmediaposter.ui.state.GeneratorScreenState
import com.devinjapan.aisocialmediaposter.ui.utils.RECENT_KEYWORD_LIST
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CaptionGeneratorViewModel @Inject constructor(
    private val textCompletionRepository: TextCompletionRepository,
    private val imageDetectorRepository: ImageDetectorRepository,
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) : ViewModel() {

    var state by mutableStateOf(GeneratorScreenState())
        private set

    init {
        getRecentList()
    }

    fun addToRecentList(item: String) {
        if (!state.recentList.contains(item)) {
            if (state.recentList.size >= 10) {
                state.recentList.removeLast()
            }
            state.recentList.add(0, item)
        }
    }

    // delete this later
    fun generateDescription() {
        state = state.copy(
            isLoading = true,
            error = null
        )
        saveRecentList()
        viewModelScope.launch {
            when (
                val result = textCompletionRepository.getReplyFromTextCompletionAPI(
                    keywords = state.loadedTags,
                    maxWords = state.selectedSocialMedia.maxTokenLimit,
                    type = state.selectedSocialMedia
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
            textCompletion = null
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
        val recentList = state.recentList
        state = GeneratorScreenState(recentList = recentList)
    }

    fun updateSelectedSocialMedia(socialMedia: SocialMedia) {
        state = state.copy(
            selectedSocialMedia = socialMedia
        )
    }

    private fun getRecentList() {
        state = state.copy(
            isLoading = true,
            error = null
        )
        viewModelScope.launch {
            val result = dataStoreRepositoryImpl.getList(RECENT_KEYWORD_LIST)
            state.recentList.addAll(result)
            state = state.copy(
                isLoading = false
            )
        }
    }

    private fun saveRecentList() {
        viewModelScope.launch {
            dataStoreRepositoryImpl.putList(RECENT_KEYWORD_LIST, state.recentList)
        }
    }
}
