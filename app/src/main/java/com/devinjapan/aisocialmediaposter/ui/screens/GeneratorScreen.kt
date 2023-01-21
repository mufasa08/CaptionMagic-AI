package com.devinjapan.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.devinjapan.aisocialmediaposter.R
import com.devinjapan.aisocialmediaposter.domain.model.SocialMedia
import com.devinjapan.aisocialmediaposter.ui.components.GeneratingDialog
import com.devinjapan.aisocialmediaposter.ui.components.ImagePicker
import com.devinjapan.aisocialmediaposter.ui.preLoadInitialImageAndTags
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBackgroundSelectedDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBackgroundSelectedLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBackgroundUnselectedDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBackgroundUnselectedLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBorderDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBorderLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.DarkChip
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.DarkChipCloseButton
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.LightChip
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.LightChipCloseButton
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TintSelectedDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TintSelectedLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TintUnselectedDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TintUnselectedLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TopBarGray
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors
import com.devinjapan.aisocialmediaposter.ui.utils.BitmapUtils
import com.devinjapan.aisocialmediaposter.ui.utils.MAX_NUMBER_OF_KEYWORDS
import com.devinjapan.aisocialmediaposter.ui.utils.ObserveLifecycleEvent
import com.devinjapan.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.svenjacobs.reveal.Reveal
import com.svenjacobs.reveal.RevealOverlayArrangement
import com.svenjacobs.reveal.RevealState
import com.svenjacobs.reveal.rememberRevealState
import kotlinx.coroutines.launch
import org.compose.museum.simpletags.SimpleTags

enum class Keys { UploadImage, AddKeyWords, SocialMedia }

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GeneratorScreen(
    navController: NavController,
    viewModel: CaptionGeneratorViewModel,
    startingImageUri: Uri? = null
) {
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

    if (startingImageUri != null && imageUri == null) {
        preLoadInitialImageAndTags(context, viewModel, startingImageUri)
        imageUri = startingImageUri
    }

    ObserveLifecycleEvent { event ->
        // 検出したイベントに応じた処理を実装する。
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.getRecentList()
                viewModel.getSelectedTone()
            }
            else -> {}
        }
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
    ) {
        val revealState = rememberRevealState()

        val localCoroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            localCoroutineScope.launch {
                revealState.reveal(Keys.UploadImage)
            }
        }

        Reveal(
            modifier = Modifier.fillMaxSize(),
            revealState = revealState,
            onRevealableClick = { key ->
                when (key) {
                    Keys.UploadImage -> {
                        localCoroutineScope.launch { revealState.reveal(Keys.AddKeyWords) }
                    }
                    Keys.AddKeyWords -> {
                        localCoroutineScope.launch { revealState.reveal(Keys.SocialMedia) }
                    }
                    Keys.SocialMedia -> {
                        localCoroutineScope.launch { revealState.hide() }
                    }
                }
            },
            onOverlayClick = { key ->
                when (key) {
                    Keys.UploadImage -> {
                        localCoroutineScope.launch { revealState.reveal(Keys.AddKeyWords) }
                    }
                    Keys.AddKeyWords -> {
                        localCoroutineScope.launch { revealState.reveal(Keys.SocialMedia) }
                    }
                    Keys.SocialMedia -> {
                        localCoroutineScope.launch { revealState.hide() }
                    }
                }
            },
            overlayContent = { key ->
                when (key) {
                    Keys.UploadImage -> {
                        Surface(
                            modifier = Modifier
                                .align(horizontalArrangement = RevealOverlayArrangement.Start)
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = RoundedCornerShape(4.dp),
                            color = Color.White
                        ) {
                            Text("This is an explanation")
                        }
                    }
                }
            }
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
                        backgroundColor =
                        if (isSystemInDarkTheme()) {
                            TopBarGray
                        } else {
                            Color.White
                        },

                        actions = {
                            IconButton(onClick = {
                                navController.navigate(context.getString(R.string.settings_screen))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    )
                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .revealable(key = Keys.UploadImage),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 16.dp),
                            text = context.getString(R.string.generator_screen_header),
                            style = MaterialTheme.typography.body2
                        )
                        if (viewModel.state.image == null) {
                            TextButton(
                                onClick = {
                                    imagePicker.launch("image/*")
                                },
                                modifier = Modifier.align(alignment = Alignment.Start)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add_photo),
                                    contentDescription = null,
                                    modifier = Modifier.size(ButtonDefaults.IconSize),
                                    tint = MaterialTheme.colors.secondary
                                )
                                Text(
                                    modifier = Modifier.padding(start = 16.dp),
                                    text = context.getString(R.string.generator_upload_text),
                                    color = MaterialTheme.colors.secondary,
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        } else {
                            ImagePicker(
                                modifier = Modifier
                                    .height(246.dp)
                                    .fillMaxWidth(),
                                viewModel,
                                viewModel.state.image!!
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.defaultMinSize(minHeight = 60.dp)
                            .revealable(key = Keys.AddKeyWords)
                    ) {
                        ListOfTags(list = viewModel.state.loadedTags, viewModel)
                        KeywordInputTextField(viewModel)
                    }

                    ListOfRecentItems(
                        list = viewModel.state.recentList.filterNot {
                            viewModel.state.loadedTags.contains(
                                it
                            )
                        },
                        viewModel = viewModel
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    SelectSocialMedia(
                        viewModel,
                        modifier = Modifier.revealable(key = Keys.SocialMedia)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.generateDescription()
                        },
                        enabled = viewModel.state.loadedTags.isNotEmpty()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_magic_wand),
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
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
}

@Composable
fun LaunchRevealTutorialIfNecessary(revealState: RevealState) {
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectSocialMedia(viewModel: CaptionGeneratorViewModel, modifier: Modifier) {
    val context = LocalContext.current
    val selectedItem = viewModel.state.selectedSocialMedia
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = context.getString(R.string.social_media_selector_label),
                style = MaterialTheme.typography.body2
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
                text = context.getString(R.string.social_media_selector_optional_label),
                style = MaterialTheme.typography.caption.copy(ThemeColors.onLightMedium)
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                GetButton(viewModel, selectedItem, SocialMedia.TWITTER, R.drawable.ic_twitter)
                Spacer(modifier = Modifier.padding(start = 8.dp))
                GetButton(viewModel, selectedItem, SocialMedia.INSTAGRAM, R.drawable.ic_instagram)
            }
        }
    }
}

@Composable
fun getButtonIconTintColor(selectedItem: SocialMedia, socialMedia: SocialMedia): Color {
    return if (isSystemInDarkTheme()) {
        if (selectedItem == socialMedia) TintSelectedDark else TintUnselectedDark
    } else {
        if (selectedItem == socialMedia) TintSelectedLight else TintUnselectedLight
    }
}

@Composable
fun getButtonBackgroundColor(selectedItem: SocialMedia, socialMedia: SocialMedia): Color {
    return if (isSystemInDarkTheme()) {
        if (selectedItem == socialMedia) ButtonBackgroundSelectedDark else ButtonBackgroundUnselectedDark
    } else {
        if (selectedItem == socialMedia) ButtonBackgroundSelectedLight else ButtonBackgroundUnselectedLight
    }
}

@Composable
fun GetButton(
    viewModel: CaptionGeneratorViewModel,
    selectedItem: SocialMedia,
    socialMedia: SocialMedia,
    icon: Int
) {
    val backgroundColor =
        getButtonBackgroundColor(selectedItem = selectedItem, socialMedia = socialMedia)
    val iconTintColor =
        getButtonIconTintColor(selectedItem = selectedItem, socialMedia = socialMedia)

    return OutlinedButton(
        onClick = {
            if (selectedItem == socialMedia) {
                viewModel.updateSelectedSocialMedia(socialMedia = SocialMedia.OTHER)
            } else {
                viewModel.updateSelectedSocialMedia(socialMedia = socialMedia)
            }
        },
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSystemInDarkTheme()) ButtonBorderDark else ButtonBorderLight
        ),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backgroundColor
        )
    ) {
        // Adding an Icon "Add" inside the Button
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = iconTintColor
        )
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

    Box(
        modifier = Modifier
            .focusRequester(focusRequester)
            .padding(start = 4.dp)
    ) {
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
                    if (text.isNotEmpty()) {
                        if (viewModel.state.loadedTags.size >= MAX_NUMBER_OF_KEYWORDS) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_max_keywords),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.addTag(text)
                            viewModel.addToRecentList(text)
                        }
                    }
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
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ListOfTags(list: List<String>, viewModel: CaptionGeneratorViewModel) {
    if (viewModel.state.isLoadingTags) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        FlowRow(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            mainAxisSpacing = 4.dp,
            crossAxisSpacing = 4.dp
        ) {
            list.forEach { tag ->
                SimpleTags(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 4.dp),
                    text = tag,
                    textStyle = MaterialTheme.typography.body2.copy(
                        textAlign = TextAlign.Start
                    ),
                    backgroundColor = if (isSystemInDarkTheme()) DarkChip else LightChip,
                    onClick = {
                        viewModel.removeTag(tag)
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_remove_tag),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp),
                            tint = if (isSystemInDarkTheme()) {
                                DarkChipCloseButton
                            } else {
                                LightChipCloseButton
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ListOfRecentItems(
    list: List<String>,
    viewModel: CaptionGeneratorViewModel
) {
    val context = LocalContext.current
    if (list.isNotEmpty()) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = context.getString(R.string.recent_list_items),
            style = MaterialTheme.typography.body2
        )

        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            mainAxisSpacing = 4.dp,
            crossAxisSpacing = 4.dp
        ) {
            list.take(6).forEach { tag ->
                SimpleTags(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 4.dp),
                    text = tag,
                    textStyle = MaterialTheme.typography.body2.copy(
                        textAlign = TextAlign.Start
                    ),
                    backgroundColor = if (isSystemInDarkTheme()) DarkChip else LightChip,
                    onClick = {
                        if (viewModel.state.loadedTags.size >= MAX_NUMBER_OF_KEYWORDS) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_max_keywords),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.addTag(tag)
                        }
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_circle),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp),
                            tint = if (isSystemInDarkTheme()) {
                                DarkChipCloseButton
                            } else {
                                LightChipCloseButton
                            }
                        )
                    }
                )
            }
        }
    }
}
