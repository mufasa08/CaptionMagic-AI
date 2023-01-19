package com.devinjapan.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.devinjapan.aisocialmediaposter.R
import com.devinjapan.aisocialmediaposter.ui.viewmodels.SettingsViewModel
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
        }
    }
}

@Composable
fun SettingsItems(viewModel: SettingsViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    SettingsMenuLink(
        title = { Text(text = "Menu 1") },
        subtitle = { Text(text = "Subtitle of menu 1") },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Menu 1"
            )
        }
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message = "Click on menu 1")
        }
    }
    Divider()
    SettingsMenuLink(
        title = { Text(text = "Menu 2") },
        subtitle = { Text(text = "Without icon") },
        action = {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message = "Action click")
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }
        }
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message = "Click on menu 2")
        }
    }
    Divider()
    SettingsMenuLink(
        title = { Text(text = "Menu 3") },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Menu 1"
            )
        }
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message = "Click on menu 3")
        }
    }
    Divider()
    var rememberCheckBoxState by remember { mutableStateOf(true) }
    SettingsMenuLink(
        title = { Text(text = "Menu 4") },
        action = {
            Checkbox(checked = rememberCheckBoxState, onCheckedChange = { newState ->
                rememberCheckBoxState = newState
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Checkbox update to: $newState"
                    )
                }
            })
        }
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message = "Click on menu 4")
        }
    }
}
