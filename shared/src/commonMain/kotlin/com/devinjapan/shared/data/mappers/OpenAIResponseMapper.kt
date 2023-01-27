package com.devinjapan.shared.data.mappers

import com.example.shared.data.source.remote.TextCompletionDto
import com.example.shared.domain.model.TextCompletion

fun TextCompletionDto.toTextCompletion(hideHashTags: Boolean): TextCompletion {
    return TextCompletion(
        choices = this.choices?.map {
            val text = it.text + if (!hideHashTags) " #ChatGPT #CaptionMagic #AI" else ""
            com.example.shared.domain.model.TextCompletion.ChoicesItem(it.index, text)
        }
    )
}
