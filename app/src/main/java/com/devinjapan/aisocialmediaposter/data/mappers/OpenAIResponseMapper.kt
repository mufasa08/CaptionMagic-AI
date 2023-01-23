package com.devinjapan.aisocialmediaposter.data.mappers

import com.devinjapan.aisocialmediaposter.data.source.remote.TextCompletionDto
import com.devinjapan.aisocialmediaposter.domain.model.TextCompletion

fun TextCompletionDto.toTextCompletion(hideHashTags: Boolean): TextCompletion {
    return TextCompletion(
        choices = this.choices?.map {
            val text = it.text + if (!hideHashTags) " #ChatGPT #captionmagic #AI" else ""
            TextCompletion.ChoicesItem(it.index, text)
        }
    )
}
