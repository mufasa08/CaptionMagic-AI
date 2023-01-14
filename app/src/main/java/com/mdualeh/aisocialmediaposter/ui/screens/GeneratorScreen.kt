package com.mdualeh.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.mdualeh.aisocialmediaposter.R
import com.mdualeh.aisocialmediaposter.domain.model.SocialMedia
import com.mdualeh.aisocialmediaposter.ui.components.GeneratingDialog
import com.mdualeh.aisocialmediaposter.ui.components.ImagePicker
import com.mdualeh.aisocialmediaposter.ui.model.SocialMediaItem
import com.mdualeh.aisocialmediaposter.ui.utils.BitmapUtils
import com.mdualeh.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel
import kotlinx.coroutines.launch
import org.compose.museum.simpletags.SimpleTags

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GeneratorScreen(navController: NavController, viewModel: CaptionGeneratorViewModel) {
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
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
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

                Spacer(modifier = Modifier.height(16.dp))
                SelectSocialMediaSpinner(viewModel)
                Spacer(modifier = Modifier.height(16.dp))
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectSocialMediaSpinner(viewModel: CaptionGeneratorViewModel) {
    val context = LocalContext.current
    val options = listOf(
        SocialMediaItem(
            context.getString(R.string.instagram),
            R.drawable.ic_instagram,
            SocialMedia.INSTAGRAM
        ),
        SocialMediaItem(
            context.getString(R.string.twitter),
            R.drawable.ic_twitter,
            SocialMedia.TWITTER
        ),
        SocialMediaItem(
            context.getString(R.string.other),
            R.drawable.ic_social_media,
            SocialMedia.OTHER
        ),

    )
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember {
        mutableStateOf(
            viewModel.state.selectedSocialMediaItem ?: options[0]
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 16.dp),
    ) {
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = context.getString(R.string.social_media_selector_label),
                style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.primary)
            )
            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedItem.itemName,
                    onValueChange = { },
                    trailingIcon = {
                        TrailingIcon(
                            expanded = expanded
                        )
                    },
                    leadingIcon = {
                        if (selectedItem.iconResId != null) {
                            Icon(
                                painter = painterResource(id = selectedItem.iconResId!!),
                                contentDescription = null,
                                modifier = Modifier.padding(start = 4.dp),
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        textColor = MaterialTheme.colors.onSurface,
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedItem = selectionOption
                                expanded = false
                                viewModel.updateSelectedSocialMedia(selectionOption)
                            }
                        ) {
                            if (selectionOption.iconResId != null) {
                                Icon(
                                    painter = painterResource(id = selectionOption.iconResId),
                                    contentDescription = null,
                                    modifier = Modifier.padding(start = 4.dp),
                                    tint = MaterialTheme.colors.onSurface,
                                )
                            }

                            Text(
                                text = selectionOption.itemName,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun KeywordInputTextField(viewModel: CaptionGeneratorViewModel) {
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
fun ListOfTags(list: List<String>, viewModel: CaptionGeneratorViewModel) {
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
        ) {
            list.forEach { tag ->
                SimpleTags(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 4.dp),
                    text = tag,
                    textStyle = MaterialTheme.typography.body2.copy(
                        textAlign = TextAlign.Start,
                    ),
                    backgroundColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
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
