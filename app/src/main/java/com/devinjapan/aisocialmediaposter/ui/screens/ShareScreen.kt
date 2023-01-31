package com.devinjapan.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
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
import com.devinjapan.aisocialmediaposter.shared.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.ui.components.BannerAd
import com.devinjapan.aisocialmediaposter.ui.components.showInterstitial
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors
import com.devinjapan.aisocialmediaposter.ui.utils.BitmapUtils.getBitmapFromContentUri
import com.devinjapan.aisocialmediaposter.ui.utils.isLandscape
import com.devinjapan.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ShareScreen(
    navController: NavController,
    viewModel: CaptionGeneratorViewModel,
    analyticsTracker: com.devinjapan.aisocialmediaposter.shared.analytics.AnalyticsTracker
) {
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
                    backgroundColor = MaterialTheme.colors.surface
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = buildString {
                            append(context.getString(R.string.share_screen_header_text_pt1))
                            append(" ")
                            append(viewModel.state.selectedCaptionTone)
                            append(" ")

                            if (viewModel.state.selectedSocialMedia == SocialMedia.OTHER) {
                                append(context.getString(R.string.share_screen_header_text_pt2))
                            } else {
                                append(
                                    viewModel.state.selectedSocialMedia.toString() + " " + context.getString(
                                        R.string.share_screen_header_text_pt2
                                    )
                                )
                            }
                            append(" ")
                            append(context.getString(R.string.share_screen_header_text_pt3))
                        },
                        style = MaterialTheme.typography.body2
                    )

                    val imageUri = viewModel.state.image
                    if (imageUri != null) {
                        val imageBitmap: Bitmap? = try {
                            getBitmapFromContentUri(
                                context.contentResolver,
                                imageUri
                            )
                        } catch (e: Exception) {
                            null
                        }

                        val isLandscape = imageBitmap?.isLandscape() == true
                        val modifier = if (isLandscape) Modifier.fillMaxWidth()
                            .wrapContentHeight() else Modifier
                            .height(246.dp)
                            .fillMaxWidth()
                        AsyncImage(
                            model = imageBitmap,
                            modifier = modifier,
                            contentScale = if (isLandscape) ContentScale.FillWidth else ContentScale.FillHeight,
                            contentDescription = "Selected image"
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
                            analyticsTracker.logEvent("description_copied", null)
                            clipboardManager.setText(AnnotatedString(text))
                            Toast.makeText(
                                context,
                                context.getString(R.string.copied_to_clipboard),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    enabled = viewModel.state.loadedTags.isNotEmpty()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_content_copy),
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = context.getString(R.string.share_screen_copy_button)
                    )
                }
                TextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        showInterstitial(context) {
                            analyticsTracker.logEvent("start_over_pressed", null)
                            viewModel.resetEverything()
                            navController.navigateUp()
                        }
                    },
                    enabled = viewModel.state.loadedTags.isNotEmpty()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = context.getString(R.string.share_screen_generate_new_one),
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body1
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
            BannerAd(
                Modifier.align(Alignment.BottomCenter),
                context.getString(R.string.ads_banner_share_screen_bottom)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun DescriptionInputTextField(viewModel: CaptionGeneratorViewModel) {
    val (focusRequester) = FocusRequester.createRefs()
    val initialText: String =
        (
            viewModel.state.modifiedText
                ?: viewModel.state.textCompletion?.choices?.first()?.text?.trim()
            ) ?: ""
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
                viewModel.updateModifiedText(text)
            },
            textStyle = MaterialTheme.typography.body2,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = if (isSystemInDarkTheme()) {
                    CustomColors.EditTextDarkBackground
                } else {
                    CustomColors.EditTextLightBackground
                },
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = MaterialTheme.colors.onSurface
            )
        )
    }
}
