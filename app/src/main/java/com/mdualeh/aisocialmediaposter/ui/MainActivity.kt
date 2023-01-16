package com.mdualeh.aisocialmediaposter.ui

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mdualeh.aisocialmediaposter.R
import com.mdualeh.aisocialmediaposter.ui.screens.GeneratorScreen
import com.mdualeh.aisocialmediaposter.ui.screens.ShareScreen
import com.mdualeh.aisocialmediaposter.ui.theme.AISocialMediaPosterTheme
import com.mdualeh.aisocialmediaposter.ui.utils.ConnectionState
import com.mdualeh.aisocialmediaposter.ui.utils.connectivityState
import com.mdualeh.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        setContent {
            AISocialMediaPosterTheme {
                // This will cause re-composition on every network state change
                val connection by connectivityState()

                val isConnected = connection === ConnectionState.Available

                if (isConnected) {
                    // Show UI when connectivity is available
                    Navigation()
                } else {
                    NotConnectedScreen()
                }
            }
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            Toast.makeText(this, getString(R.string.not_showing_notifications), Toast.LENGTH_SHORT)
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    POST_NOTIFICATIONS
                )
            ) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun Navigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    val viewModel = hiltViewModel<CaptionGeneratorViewModel>()

    NavHost(
        navController = navController,
        startDestination = context.getString(R.string.generator_screen)
    ) {
        composable(context.getString(R.string.generator_screen)) {
            GeneratorScreen(navController = navController, viewModel = viewModel)
        }
        composable(context.getString(R.string.share_screen)) {
            ShareScreen(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun NotConnectedScreen() {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_wifi_offline),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = context.getString(R.string.offline),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
        }
    }
}
