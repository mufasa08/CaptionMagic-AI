package com.devinjapan.aisocialmediaposter.data.mappers

import com.devinjapan.aisocialmediaposter.data.source.remote.TextCompletionDto
import com.devinjapan.aisocialmediaposter.domain.model.TextCompletion

fun TextCompletionDto.toTextCompletion(): TextCompletion {
    return TextCompletion(
        choices = this.choices?.map { TextCompletion.ChoicesItem(it.index, it.text) }
    )
}
