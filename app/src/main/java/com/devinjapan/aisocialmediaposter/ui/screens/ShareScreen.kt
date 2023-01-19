package com.devinjapan.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.devinjapan.aisocialmediaposter.R
import com.devinjapan.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ShareScreen(navController: NavController, viewModel: CaptionGeneratorViewModel) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    BackHandler(enabled = true, onBack = {
        viewModel.clearGeneratedText()
        navController.navigateUp()
    })

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                TopAppBar(
                    elevation = 0.dp,
                    title = {
                        Text(
                            context.getString(R.string.app_name),
                            style = MaterialTheme.typography.h6
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.clearGeneratedText()
                            navController.navigateUp()
                        }) {
                            Icon(Icons.Filled.ArrowBack, null)
                        }
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
                        text = context.getString(R.string.share_screen_header_text),
                        style = MaterialTheme.typography.caption
                    )
                    if (viewModel.state.image != null) {
                        AsyncImage(
                            model = viewModel.state.image,
                            modifier = Modifier
                                .height(246.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Fit,
                            contentDescription = "Selected image",
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                DescriptionInputTextField(viewModel)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        val text = viewModel.state.modifiedText
                            ?: viewModel.state.textCompletion?.choices?.first()?.text?.trim()
                        if (text != null) {
                            clipboardManager.setText(AnnotatedString(text))
                            Toast.makeText(
                                context,
                                context.getString(R.string.copied_to_clipboard),
                                Toast.LENGTH_SHORT
                            )
                        }
                    },
                    enabled = viewModel.state.loadedTags.isNotEmpty()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_content_copy),
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = context.getString(R.string.share_screen_copy_button)
                    )
                }
                TextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        viewModel.resetEverything()
                        navController.navigateUp()
                    },
                    enabled = viewModel.state.loadedTags.isNotEmpty()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = context.getString(R.string.share_screen_generate_new_one),
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun DescriptionInputTextField(viewModel: CaptionGeneratorViewModel) {
    val context = LocalContext.current
    val (focusRequester) = FocusRequester.createRefs()
    val initialText: String =
        viewModel.state.textCompletion?.choices?.first()?.text?.trim() ?: "no text"
    var text by rememberSaveable { mutableStateOf(initialText) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .focusRequester(focusRequester)
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            textStyle = MaterialTheme.typography.body2,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.updateModifiedText(text)
                    text = ""
                }
            ),
            modifier = Modifier
                .padding(0.dp)
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = MaterialTheme.colors.onSurface,
            )
        )
    }
}
