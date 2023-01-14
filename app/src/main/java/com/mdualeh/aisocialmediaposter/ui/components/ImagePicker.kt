package com.mdualeh.aisocialmediaposter.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mdualeh.aisocialmediaposter.R
import com.mdualeh.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel

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
            modifier = Modifier.fillMaxWidth().fillMaxHeight().align(Alignment.TopCenter),
            contentScale = ContentScale.Fit,
            contentDescription = "Selected image",
        )
        IconButton(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp), onClick = {
            viewModel.clearBitmap()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close_round), null)
        }
    }
}
