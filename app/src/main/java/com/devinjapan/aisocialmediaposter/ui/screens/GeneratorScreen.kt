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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.devinjapan.aisocialmediaposter.R
import com.devinjapan.aisocialmediaposter.ui.components.BannerAd
import com.devinjapan.aisocialmediaposter.ui.components.ErrorDialog
import com.devinjapan.aisocialmediaposter.ui.components.GeneratingDialog
import com.devinjapan.aisocialmediaposter.ui.components.ImagePicker
import com.devinjapan.aisocialmediaposter.ui.preLoadInitialImageAndTags
import com.devinjapan.aisocialmediaposter.ui.state.GeneratorScreenState
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBackgroundSelectedDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBackgroundSelectedLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBackgroundUnselectedDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBackgroundUnselectedLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBorderDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.ButtonBorderLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.DarkChip
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.DarkChipCloseButton
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.LightBlueChip
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.LightChip
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.LightChipCloseButton
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TintSelectedDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TintSelectedLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TintUnselectedDark
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TintUnselectedLight
import com.devinjapan.aisocialmediaposter.ui.theme.CustomColors.TopBarGray
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors
import com.devinjapan.aisocialmediaposter.ui.utils.*
import com.devinjapan.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel
import com.devinjapan.shared.analytics.AnalyticsTracker
import com.devinjapan.shared.data.error.ApiException
import com.devinjapan.shared.data.error.ImageDetectionException
import com.devinjapan.shared.domain.model.SocialMedia
import com.devinjapan.shared.util.BitmapUtils
import com.google.accompanist.flowlayout.FlowRow
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.launch
import org.compose.museum.simpletags.SimpleTags

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GeneratorScreen(
    navController: NavController,
    viewModel: CaptionGeneratorViewModel,
    startingImageUri: Uri? = null,
    analyticsTracker: AnalyticsTracker
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
        preLoadInitialImageAndTags(viewModel, startingImageUri)
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
                analyticsTracker.logEvent("image_uploaded", null)
                imageUri = uri
                imageUri?.encodedPath?.let { viewModel.processImage(it) }
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
                        .padding(vertical = 8.dp, horizontal = 16.dp),
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
                        val imageUri = viewModel.state.image
                        val imageBitmap =
                            BitmapUtils.getBitmapFromContentUri(
                                context.contentResolver,
                                Uri.parse(imageUri)
                            )

                        val isLandscape = imageBitmap?.isLandscape() == true
                        val modifier = if (isLandscape) Modifier.fillMaxWidth()
                            .wrapContentHeight() else Modifier
                            .height(246.dp)
                            .fillMaxWidth()
                        if (imageBitmap != null) {
                            ImagePicker(
                                modifier = modifier,
                                viewModel,
                                imageBitmap,
                                isLandscape
                            )
                        }
                    }
                }

                Column(modifier = Modifier.defaultMinSize(minHeight = 60.dp)) {
                    ListOfTags(list = viewModel.state.loadedTags, viewModel)
                    KeywordInputTextField(viewModel)
                }

                ListOfRecentItems(
                    list = viewModel.state.recentList.minus(viewModel.state.loadedTags.toSet()),
                    viewModel
                )

                Spacer(modifier = Modifier.height(16.dp))

                SelectSocialMedia(viewModel)

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
                        text = context.getString(R.string.generator_generate_post_button),
                        letterSpacing = 1.5.sp
                    )
                }
            }
            if (viewModel.state.launchNumber > 1) {
                BannerAd(
                    Modifier.align(BottomCenter),
                    adUnitId = context.getString(R.string.ads_banner_generator_screen_bottom)
                )
            }
            if (viewModel.state.isLoading) {
                GeneratingDialog()
            }

            viewModel.state.error?.let { error ->
                if (error.exception != null && error.exception is ApiException) {
                    ErrorDialog(
                        errorMessage = error.exception.toUserUnderstandableMessage(),
                        onConfirmClick = {
                            viewModel.clearError()
                        }
                    )
                } else {
                    if (error.exception is ImageDetectionException) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.exception_message_image_detection),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, error.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectSocialMedia(viewModel: CaptionGeneratorViewModel) {
    val context = LocalContext.current
    val selectedItem = viewModel.state.selectedSocialMedia
    Box(
        modifier = Modifier
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
                style = MaterialTheme.typography.caption.copy(
                    if (isSystemInDarkTheme()) ThemeColors.onDarkMedium else ThemeColors.onLightMedium
                )
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                GetButton(
                    viewModel,
                    selectedItem,
                    SocialMedia.TWITTER,
                    R.drawable.ic_twitter
                )
                Spacer(modifier = Modifier.padding(start = 8.dp))
                GetButton(
                    viewModel,
                    selectedItem,
                    SocialMedia.INSTAGRAM,
                    R.drawable.ic_instagram
                )
            }
        }
    }
}

@Composable
fun getButtonIconTintColor(
    selectedItem: SocialMedia,
    socialMedia: SocialMedia
): Color {
    return if (isSystemInDarkTheme()) {
        if (selectedItem == socialMedia) TintSelectedDark else TintUnselectedDark
    } else {
        if (selectedItem == socialMedia) TintSelectedLight else TintUnselectedLight
    }
}

@Composable
fun getButtonBackgroundColor(
    selectedItem: SocialMedia,
    socialMedia: SocialMedia
): Color {
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
        Column() {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight().testTag("keyword")
            ) {
                TextField(
                    value = text,
                    onValueChange = {
                        viewModel.validateKeyword(it)
                        if (text.length < MAX_KEYWORD_LENGTH + 1 || it.length < text.length) {
                            text = it
                        }
                    },
                    placeholder = { Text(context.getString(R.string.generator_keyword_hint)) },
                    textStyle = MaterialTheme.typography.body2,
                    isError = viewModel.state.keywordError != GeneratorScreenState.ValidationError.NONE,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (text.isNotEmpty() && viewModel.state.keywordError == GeneratorScreenState.ValidationError.NONE) {
                                if (viewModel.state.loadedTags.size >= MAX_KEYWORDS) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.toast_max_keywords),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.addTag(text)
                                    viewModel.addToRecentList(text)
                                }
                                text = ""
                            }
                        }
                    ),
                    maxLines = 1,
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
                if (text.isNotEmpty()) {
                    Text(
                        text = "${text.length} / $MAX_KEYWORD_LENGTH",
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(CenterVertically)
                            .padding(end = 16.dp),
                        color = if (MAX_KEYWORD_LENGTH - text.length < 0) {
                            Color.Red
                        } else if (MAX_KEYWORD_LENGTH - text.length < 5) Color.Yellow
                        else MaterialTheme.colors.onPrimary
                    )
                }
            }
            if (viewModel.state.keywordError != GeneratorScreenState.ValidationError.NONE) {
                val errorText = when (viewModel.state.keywordError) {
                    GeneratorScreenState.ValidationError.TOO_MANY_KEYWORDS -> {
                        context.getString(R.string.validation_text_keyword_limit)
                    }
                    else -> {
                        context.getString(R.string.validation_text_too_long)
                    }
                }

                Text(
                    text = errorText,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ListOfTags(list: List<String>, viewModel: CaptionGeneratorViewModel) {
    if (viewModel.state.isLoadingTags) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
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
                        textAlign = TextAlign.Start,
                        color = ThemeColors.onLightHigh
                    ),
                    backgroundColor = LightBlueChip,
                    onClick = {
                        viewModel.removeTag(tag)
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_remove_tag),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp),
                            tint = LightChipCloseButton
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ListOfRecentItems(list: List<String>, viewModel: CaptionGeneratorViewModel) {
    val context = LocalContext.current
    if (list.isNotEmpty()) {
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
                        .padding(end = 4.dp),
                    text = tag,
                    textStyle = MaterialTheme.typography.body2.copy(
                        textAlign = TextAlign.Start,
                        color = if (isSystemInDarkTheme()) ThemeColors.onDarkMedium else ThemeColors.onLightMedium
                    ),
                    backgroundColor = if (isSystemInDarkTheme()) DarkChip else LightChip,
                    onClick = {
                        if (viewModel.state.loadedTags.size >= MAX_KEYWORDS) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_max_keywords),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.addTag(tag)
                        }
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
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
