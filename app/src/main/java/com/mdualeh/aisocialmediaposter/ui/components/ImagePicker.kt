package com.mdualeh.aisocialmediaposter.ui.components

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mdualeh.aisocialmediaposter.R
import com.mdualeh.aisocialmediaposter.ui.utils.BitmapUtils
import com.mdualeh.aisocialmediaposter.ui.viewmodels.TextCompletionViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import java.io.IOException

@Preview
@Composable
fun ImagePicker(
    modifier: Modifier,
    // change this sometime.. don't pass viewmodel but pass a callback.
    viewModel: TextCompletionViewModel,
) {
    val contentResolver = LocalContext.current.contentResolver
    // 1
    var hasImage by remember {
        mutableStateOf(false)
    }
    // 2
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            // 3
            hasImage = uri != null
            imageUri = uri
        }
    )

    Box(
        modifier = modifier,
    ) {
        // 4
        if (hasImage && imageUri != null) {
            // 5
            try {
                val imageBitmap = BitmapUtils.getBitmapFromContentUri(contentResolver, imageUri) ?: return
                viewModel.processBitmap(imageBitmap)
            } catch (e: IOException) {
                Log.e(
                    ContentValues.TAG,
                    "Error retrieving saved image"
                )
            }
            AsyncImage(
                model = imageUri,
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
                    .clickable {
                        imagePicker.launch("image/*")
                    },
                contentScale = ContentScale.Fit,
                contentDescription = "Selected image",
            )
        } else {
            CoilImage(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        imagePicker.launch("image/*")
                    },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                ),
                imageModel = { R.drawable.ic_image_select },
            )
        }
    }
}
