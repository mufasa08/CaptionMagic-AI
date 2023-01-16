package com.devinjapan.aisocialmediaposter.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devinjapan.aisocialmediaposter.R
import com.devinjapan.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel

@Preview
@Composable
fun ImagePicker(
    modifier: Modifier,
    // change this sometime.. don't pass viewmodel but pass a callback.
    viewModel: CaptionGeneratorViewModel,
    imageBitmap: Bitmap,
) {
    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = imageBitmap,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Fit,
            contentDescription = "Selected image",
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp),
            onClick = {
                viewModel.clearBitmap()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close_round), null
            )
        }
    }
}