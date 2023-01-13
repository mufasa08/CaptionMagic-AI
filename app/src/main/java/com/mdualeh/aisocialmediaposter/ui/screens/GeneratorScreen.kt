package com.mdualeh.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.mdualeh.aisocialmediaposter.R
import com.mdualeh.aisocialmediaposter.ui.components.GeneratingDialog
import com.mdualeh.aisocialmediaposter.ui.components.ImagePicker
import com.mdualeh.aisocialmediaposter.ui.utils.BitmapUtils
import com.mdualeh.aisocialmediaposter.ui.viewmodels.TextCompletionViewModel
import kotlinx.coroutines.launch
import org.compose.museum.simpletags.SimpleTags

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GeneratorScreen(navController: NavController, viewModel: TextCompletionViewModel) {
    val context = LocalContext.current
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

            if (hasImage) {
                imageUri = uri
                val imageBitmap =
                    BitmapUtils.getBitmapFromContentUri(context.contentResolver, imageUri)
                if (imageBitmap != null) {
                    viewModel.processBitmap(imageBitmap)
                }
            }
        }
    )

    if (viewModel.state.textCompletion != null) {
        LaunchedEffect(Unit) {
            navController.navigate(context.getString(R.string.share_screen))
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.Top) {
                TopAppBar(
                    elevation = 0.dp,
                    title = {
                        Text(
                            context.getString(R.string.app_name),
                            style = MaterialTheme.typography.h6
                        )
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start,
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
                            modifier = Modifier.height(246.dp).fillMaxWidth(), viewModel,
                            viewModel.state.image!!
                        )
                    }
                }

                ListOfTags(list = viewModel.state.loadedTags, viewModel)

                KeywordInputTextField(viewModel)
                Button(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    onClick = {
                        viewModel.testGeneratorApi()
                    },
                    enabled = viewModel.state.loadedTags.isNotEmpty()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_magic_wand),
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = context.getString(R.string.generator_generate_post_button)
                    )
                }
            }
            if (viewModel.state.isLoading) {
                GeneratingDialog()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun KeywordInputTextField(viewModel: TextCompletionViewModel) {
    val context = LocalContext.current
    val (focusRequester) = FocusRequester.createRefs()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.focusRequester(focusRequester).padding(start = 4.dp)) {
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            placeholder = { Text(context.getString(R.string.generator_keyword_hint)) },
            textStyle = MaterialTheme.typography.body2,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.addTag(text)
                    text = ""
                }
            ),
            modifier = Modifier.padding(0.dp).bringIntoViewRequester(bringIntoViewRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = MaterialTheme.colors.onSurface,
            )
        )
    }
}

@Composable
fun ListOfTags(list: List<String>, viewModel: TextCompletionViewModel) {
    if (viewModel.state.isLoadingTags) {
        Box(Modifier.fillMaxWidth().padding(16.dp)) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        FlowRow(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            mainAxisSpacing = 4.dp,
            crossAxisSpacing = 4.dp,
            mainAxisAlignment = MainAxisAlignment.Center,
        ) {
            list.forEach { tag ->
                SimpleTags(
                    modifier = Modifier.wrapContentHeight().padding(horizontal = 4.dp),
                    text = tag,
                    textStyle = MaterialTheme.typography.body2.copy(
                        textAlign = TextAlign.Start,
                    ),
                    onClick = {
                        viewModel.removeTag(tag)
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_remove_tag),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp),
                        )
                    }
                )
            }
        }
    }
}
