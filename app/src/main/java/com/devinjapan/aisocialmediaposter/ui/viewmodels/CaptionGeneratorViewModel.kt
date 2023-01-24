package com.devinjapan.aisocialmediaposter.ui.viewmodels

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devinjapan.aisocialmediaposter.analytics.AnalyticsTracker
import com.devinjapan.aisocialmediaposter.data.repository.DataStoreRepositoryImpl
import com.devinjapan.aisocialmediaposter.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.domain.repository.ImageDetectorRepository
import com.devinjapan.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.devinjapan.aisocialmediaposter.domain.util.Resource
import com.devinjapan.aisocialmediaposter.ui.state.GeneratorScreenState
import com.devinjapan.aisocialmediaposter.ui.utils.MAX_KEYWORDS
import com.devinjapan.aisocialmediaposter.ui.utils.MAX_KEYWORD_LENGTH
import com.devinjapan.aisocialmediaposter.ui.utils.RECENT_KEYWORD_LIST
import com.devinjapan.aisocialmediaposter.ui.utils.SELECTED_TONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CaptionGeneratorViewModel @Inject constructor(
    private val textCompletionRepository: TextCompletionRepository,
    private val imageDetectorRepository: ImageDetectorRepository,
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    var state by mutableStateOf(GeneratorScreenState())
        private set

    fun addToRecentList(item: String) {
        if (!state.recentList.contains(item)) {
            if (state.recentList.size >= 10) {
                state.recentList.removeLast()
            }
            state.recentList.add(0, item)
        }
    }

    fun generateDescription() {
        analyticsTracker.logEvent("generate_description", null)
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            saveRecentList()
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
                    if (result.message != null) {
                        state = state.copy(
                            textCompletion = null,
                            isLoading = false,
                            error = GeneratorScreenState.ErrorInfo(result.message, result.exception)

                        )
                    }
                }
            }
        }
    }

    fun processBitmap(resizedBitmap: Bitmap) {
        viewModelScope.launch {
            state = state.copy(
                image = resizedBitmap,
                isLoadingTags = true,
                error = null
            )
            when (val result = imageDetectorRepository.getTagsFromImage(resizedBitmap)) {
                is Resource.Success -> {
                    result.data?.let {
                        val availableTagSlots = MAX_KEYWORDS - state.loadedTags.size
                        val list = it.take(availableTagSlots)
                        state.loadedTags.addAll(list)
                        state = state.copy(
                            isLoadingTags = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    state.loadedTags.clear()
                    if (result.message != null) {
                        state = state.copy(
                            isLoadingTags = false,
                            error = GeneratorScreenState.ErrorInfo(result.message, result.exception)
                        )
                    }
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
        if (state.loadedTags.size < MAX_KEYWORDS && tag.isNotEmpty()) {
            state.loadedTags.add(tag)
        }
    }

    fun removeTag(tag: String) {
        state.loadedTags.remove(tag)
        if (state.loadedTags.size <= MAX_KEYWORDS) {
            state = state.copy(
                keywordError = GeneratorScreenState.ValidationError.NONE
            )
        }
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

    fun getRecentList() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            state.recentList.clear()
            val result =
                dataStoreRepositoryImpl.getList(RECENT_KEYWORD_LIST).filter { it.isNotEmpty() }
            if (result.isNotEmpty()) {
                state.recentList.addAll(result)
            }
            state = state.copy(
                isLoading = false
            )
        }
    }

    fun getSelectedTone() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            val selectedTone = dataStoreRepositoryImpl.getString(SELECTED_TONE) ?: "Cool"
            state = state.copy(
                selectedCaptionTone = selectedTone,
                isLoading = false
            )
        }
    }

    private fun saveRecentList() {
        viewModelScope.launch {
            if (state.recentList.isNotEmpty()) {
                dataStoreRepositoryImpl.putList(RECENT_KEYWORD_LIST, state.recentList)
            }
        }
    }

    fun clearError() {
        state = state.copy(
            error = null
        )
    }

    fun updateConnectionStatus(connected: Boolean) {
        state = state.copy(
            isConnected = connected
        )
    }

    fun validateKeyword(keyword: String) {
        val keywordErrorState = if (state.loadedTags.size >= MAX_KEYWORDS) {
            GeneratorScreenState.ValidationError.TOO_MANY_KEYWORDS
        } else if (keyword.length > MAX_KEYWORD_LENGTH) {
            GeneratorScreenState.ValidationError.TOO_LONG
        } else {
            GeneratorScreenState.ValidationError.NONE
        }
        state = state.copy(
            keywordError = keywordErrorState
        )
    }

    companion object {
    }
}
