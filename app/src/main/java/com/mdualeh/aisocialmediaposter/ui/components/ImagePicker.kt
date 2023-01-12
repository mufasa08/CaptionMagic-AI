package com.mdualeh.aisocialmediaposter.ui.components

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mdualeh.aisocialmediaposter.ui.utils.BitmapUtils
import com.mdualeh.aisocialmediaposter.ui.viewmodels.TextCompletionViewModel
import java.io.IOException

@Preview
@Composable
fun ImagePicker(
    modifier: Modifier,
    // change this sometime.. don't pass viewmodel but pass a callback.
    viewModel: TextCompletionViewModel,
    imageBitmap: Bitmap,
) {
    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = imageBitmap,
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
            contentScale = ContentScale.Fit,
            contentDescription = "Selected image",
        )
        IconButton(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp), onClick = {
            viewModel.clearBitmap()
        }) {
            Icon(Icons.Filled.Close, null)
        }
    }
}
