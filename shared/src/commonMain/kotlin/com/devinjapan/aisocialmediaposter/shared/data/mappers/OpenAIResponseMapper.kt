package com.devinjapan.aisocialmediaposter.shared.data.mappers

import com.devinjapan.aisocialmediaposter.shared.data.request.TextCompletionDto
import com.devinjapan.aisocialmediaposter.shared.domain.model.TextCompletion

fun TextCompletionDto.toTextCompletion(hideHashTags: Boolean): TextCompletion {
    return TextCompletion(
        choices = this.choices?.map {
            val text = it.text + if (!hideHashTags) " #ChatGPT #CaptionMagic #AI" else ""
            com.devinjapan.aisocialmediaposter.shared.domain.model.TextCompletion.ChoicesItem(
                it.index,
                text
            )
        }
    )
}
