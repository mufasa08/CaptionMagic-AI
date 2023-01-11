package com.mdualeh.aisocialmediaposter.data.mappers

import com.mdualeh.aisocialmediaposter.data.remote.TextCompletionDto
import com.mdualeh.aisocialmediaposter.domain.weather.TextCompletion

fun TextCompletionDto.toTextCompletion(): TextCompletion {
    return TextCompletion(
        choices = this.choices?.map { TextCompletion.ChoicesItem(it.index, it.text) }
    )
}
