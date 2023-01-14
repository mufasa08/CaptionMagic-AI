package com.mdualeh.aisocialmediaposter.data.mappers

import com.mdualeh.aisocialmediaposter.data.source.remote.TextCompletionDto
import com.mdualeh.aisocialmediaposter.domain.model.TextCompletion

fun TextCompletionDto.toTextCompletion(): TextCompletion {
    return TextCompletion(
        choices = this.choices?.map { TextCompletion.ChoicesItem(it.index, it.text) }
    )
}
