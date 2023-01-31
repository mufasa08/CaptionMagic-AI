package com.devinjapan.aisocialmediaposter.ui.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devinjapan.aisocialmediaposter.shared.domain.repository.DataStoreRepository
import com.devinjapan.aisocialmediaposter.shared.domain.repository.ImageDetectorRepository
import com.devinjapan.aisocialmediaposter.shared.domain.repository.TextCompletionRepository
import com.devinjapan.aisocialmediaposter.shared.domain.util.Resource
import com.devinjapan.aisocialmediaposter.shared.domain.util.SELECTED_TONE
import com.devinjapan.aisocialmediaposter.ui.state.GeneratorScreenState
import com.devinjapan.aisocialmediaposter.ui.utils.LAUNCH_COUNT
import com.devinjapan.aisocialmediaposter.ui.utils.MAX_KEYWORDS
import com.devinjapan.aisocialmediaposter.ui.utils.MAX_KEYWORD_LENGTH
import com.devinjapan.aisocialmediaposter.ui.utils.RECENT_KEYWORD_LIST
import kotlinx.coroutines.launch

class CaptionGeneratorViewModel(
    private val textCompletionRepository: TextCompletionRepository,
    private val imageDetectorRepository: ImageDetectorRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val analyticsTracker: com.devinjapan.aisocialmediaposter.shared.analytics.AnalyticsTracker
) : ViewModel() {
    var state by mutableStateOf(GeneratorScreenState())
        private set

    init {
        checkLaunchCountAndIncrement()
    }

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
                is Resource.Success<*> -> {
                    state = state.copy(
                        textCompletion = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error<*> -> {
                    val message = result.message
                    if (message != null) {
                        state = state.copy(
                            textCompletion = null,
                            isLoading = false,
                            error = GeneratorScreenState.ErrorInfo(message, result.exception)
                        )
                    }
                }
            }
        }
    }

    fun processImage(imageUri: Uri) {
        viewModelScope.launch {
            state = state.copy(
                image = imageUri,
                isLoadingTags = true,
                error = null
            )
            when (val result = imageDetectorRepository.getTagsFromImage(imageUri.toString())) {
                is Resource.Success<*> -> {
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
                is Resource.Error<*> -> {
                    state.loadedTags.clear()
                    val message = result.message
                    if (message != null) {
                        state = state.copy(
                            isLoadingTags = false,
                            error = GeneratorScreenState.ErrorInfo(message, result.exception)
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
            textCompletion = null,
            modifiedText = null
        )
    }

    fun updateModifiedText(text: String) {
        state.modifiedText = text
    }

    fun resetEverything() {
        state = state.copy(
            image = null,
            textCompletion = null,
            modifiedText = null
        )
        state.loadedTags.clear()
        state.recentList.clear()
    }

    fun updateSelectedSocialMedia(socialMedia: com.devinjapan.aisocialmediaposter.shared.domain.model.SocialMedia) {
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
                dataStoreRepository.getList(RECENT_KEYWORD_LIST).filter { it.isNotEmpty() }
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
            val selectedTone = dataStoreRepository.getString(SELECTED_TONE) ?: "Cool"
            state = state.copy(
                selectedCaptionTone = selectedTone,
                isLoading = false
            )
        }
    }

    private fun saveRecentList() {
        viewModelScope.launch {
            if (state.recentList.isNotEmpty()) {
                dataStoreRepository.putList(RECENT_KEYWORD_LIST, state.recentList)
            }
        }
    }

    fun clearError() {
        state = state.copy(
            error = null
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

    private fun checkLaunchCountAndIncrement() {
        viewModelScope.launch {
            val launchCount = dataStoreRepository.getLong(LAUNCH_COUNT) ?: 1L
            dataStoreRepository.putLong(LAUNCH_COUNT, launchCount + 1L)
            state = state.copy(
                isLoadingFirstLaunchCheck = false,
                launchNumber = launchCount,
                isFirstLaunch = launchCount == 1L
            )
        }
    }

    fun finishOnBoarding() {
        state = state.copy(
            isFirstLaunch = false
        )
    }
}
