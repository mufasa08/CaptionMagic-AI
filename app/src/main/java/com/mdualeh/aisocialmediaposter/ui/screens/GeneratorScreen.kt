package com.mdualeh.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.mdualeh.aisocialmediaposter.R
import com.mdualeh.aisocialmediaposter.ui.components.ImagePicker
import com.mdualeh.aisocialmediaposter.ui.utils.BitmapUtils
import com.mdualeh.aisocialmediaposter.ui.viewmodels.TextCompletionViewModel
import org.compose.museum.simpletags.SimpleTags

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GeneratorScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<TextCompletionViewModel>()
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

            val imageBitmap = BitmapUtils.getBitmapFromContentUri(context.contentResolver, imageUri)
            if (imageBitmap != null) {
                viewModel.processBitmap(imageBitmap)
            }
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column() {
            TopAppBar(
                elevation = 0.dp,
                title = {
                    Text(context.getString(R.string.app_name), style = MaterialTheme.typography.h6)
                },
                backgroundColor = MaterialTheme.colors.surface,
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = context.getString(R.string.generator_screen_header),
                    style = MaterialTheme.typography.caption
                )
                if (viewModel.state.image == null) {
                    TextButton(
                        onClick = {
                            imagePicker.launch("image/*")
                        },
                        modifier = Modifier.align(alignment = Alignment.Start),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_photo),
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            tint = MaterialTheme.colors.secondary,
                        )
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = context.getString(R.string.generator_upload_text),
                            color = MaterialTheme.colors.secondary,
                        )
                    }
                } else {
                    ImagePicker(
                        modifier = Modifier.height(400.dp).fillMaxWidth(), viewModel,
                        viewModel.state.image!!
                    )
                }
                ListOfTags(list = viewModel.state.loadedTags)
                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        // generate post
                    },
                ) {
                    Text(
                        text = "Generate Post"
                    )
                }
            }
        }
    }
}

@Composable
fun ListOfTags(list: List<String>) {
    FlowRow {
        list.forEach { tag ->
            SimpleTags(modifier = Modifier.wrapContentWidth().padding(4.dp), text = tag, onClick = {
            })
        }
    }
}
