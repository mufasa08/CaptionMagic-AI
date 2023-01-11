package com.mdualeh.aisocialmediaposter.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdualeh.aisocialmediaposter.domain.repository.TextCompletionRepository
import com.mdualeh.aisocialmediaposter.domain.util.Resource
import com.mdualeh.aisocialmediaposter.ui.TextCompletionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextCompletionViewModel @Inject constructor(
    private val repository: TextCompletionRepository,
) : ViewModel() {

    var state by mutableStateOf(TextCompletionState())
        private set

    // delete this later
    fun testGeneratorApi() {
        viewModelScope.launch {
            when (
                val result = repository.getReplyFromTextCompletionAPI(
                    keywords = listOf(
                        "instagram post",
                        "ramen",
                        "developer in japan",
                        "nature",
                        "mountains"
                    ),
                    maxWords = INSTAGRAM_MAX_CHAR_LIMIT
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

    companion object {
        const val INSTAGRAM_MAX_CHAR_LIMIT = 2200
    }
}
