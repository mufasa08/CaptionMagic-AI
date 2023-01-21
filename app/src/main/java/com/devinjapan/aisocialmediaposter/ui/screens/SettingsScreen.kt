package com.devinjapan.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.devinjapan.aisocialmediaposter.BuildConfig
import com.devinjapan.aisocialmediaposter.R
import com.devinjapan.aisocialmediaposter.ui.components.CustomSettingsList
import com.devinjapan.aisocialmediaposter.ui.components.GeneratingDialog
import com.devinjapan.aisocialmediaposter.ui.utils.BUG_REPORT_BASE_URL
import com.devinjapan.aisocialmediaposter.ui.utils.FEEDBACK_URL
import com.devinjapan.aisocialmediaposter.ui.viewmodels.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current

    val viewModel = hiltViewModel<SettingsViewModel>()

    BackHandler(enabled = true, onBack = {
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
                            context.getString(R.string.settings_title),
                            style = MaterialTheme.typography.h6
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(Icons.Filled.ArrowBack, null)
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface
                )
                SettingsItems(viewModel)

                //
            }

            if (viewModel.state.isLoading) {
                GeneratingDialog()
            }
        }
    }
}

@Composable
fun SettingsItems(viewModel: SettingsViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val currentFirebaseUid: String = FirebaseAuth.getInstance().currentUser?.uid ?: "no-id"

    SettingsGroup(title = { Text(text = context.getString(R.string.settings_customize)) }) {
        val list = listOf<String>(
            context.getString(R.string.setting_tone_cool) + " \uD83D\uDE0E",

            context.getString(R.string.setting_tone_humurous) + " \uD83E\uDD23",

            context.getString(R.string.setting_tone_poetic) + " \uD83D\uDCDC\uD83D\uDD8B",

            context.getString(R.string.setting_tone_professional) + " \uD83D\uDCBC",

            context.getString(R.string.setting_tone_gothic) + " ꧁ ༺ ༻ ꧂",

            context.getString(R.string.setting_tone_energetic) + " ⚡",

            context.getString(R.string.setting_tone_flirty) + " \uD83D\uDE18"
        )
        var selectedNumber: Int = list.indexOf(viewModel.state.selectedCaptionTone)
        if (selectedNumber == -1) selectedNumber = 0
        val singleChoiceState = rememberIntSettingState(defaultValue = selectedNumber)
        CustomSettingsList(
            state = singleChoiceState,
            title = { Text(text = context.getString(R.string.setting_tone_select_preferred)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mood),
                    contentDescription = "Menu Tone"
                )
            },
            items = list,
            onClick = {
                viewModel.updateSelectedTone(list[singleChoiceState.value])
            }
        )
    }
    SettingsGroup(title = { Text(text = context.getString(R.string.settings_group_data)) }) {
        SettingsMenuLink(
            title = { Text(text = context.getString(R.string.settings_title_recent_keywords)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_history),
                    contentDescription = "Menu Clear Data"
                )
            }
        ) {
            coroutineScope.launch {
                // TODO log firebase uid analytics
                viewModel.clearRecentList()
                Toast.makeText(
                    context,
                    context.getString(R.string.settings_toast_keywords_cleared),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    SettingsGroup(title = { Text(text = context.getString(R.string.settings_group_other)) }) {
        SettingsMenuLink(
            title = { Text(text = context.getString(R.string.settings_title_bug_report)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bug_report),
                    contentDescription = "Menu Bug Report"
                )
            }
        ) {
            // TODO log firebase uid analytics
            uriHandler.openUri(BUG_REPORT_BASE_URL)
        }

        SettingsMenuLink(
            title = { Text(text = context.getString(R.string.settings_title_feedback)) },
            subtitle = { Text(text = context.getString(R.string.settings_subtitle_feedback)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_feedback),
                    contentDescription = "Menu Feedback"
                )
            }
        ) {
            // TODO log firebase uid analytics
            uriHandler.openUri(FEEDBACK_URL)
        }
    }

    SettingsGroup(title = { Text(text = context.getString(R.string.settings_group_about)) }) {
        SettingsMenuLink(
            modifier = Modifier.clickable(enabled = false, onClick = {}),
            title = { Text(text = context.getString(R.string.app_name)) },
            subtitle = { Text(text = "Version: ${BuildConfig.VERSION_NAME}") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "About Menu"
                )
            }
        ) {
        }
    }

    if (BuildConfig.DEBUG) {
        SettingsGroup(title = { Text(text = "DEBUG ONLY VISIBLE") }) {
            SettingsMenuLink(
                modifier = Modifier.clickable(enabled = false, onClick = {}),
                title = { Text(text = "Reset First Launch Relay") }
            ) {
                coroutineScope.launch {
                    viewModel.resetFirstLaunchRelay()
                    Toast.makeText(
                        context,
                        "First Launch Relay Reset",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
