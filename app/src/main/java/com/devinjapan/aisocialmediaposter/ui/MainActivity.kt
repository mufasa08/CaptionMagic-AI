package com.devinjapan.aisocialmediaposter.ui

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devinjapan.aisocialmediaposter.R
import com.devinjapan.aisocialmediaposter.analytics.AnalyticsTracker
import com.devinjapan.aisocialmediaposter.ui.screens.GeneratorScreen
import com.devinjapan.aisocialmediaposter.ui.screens.SettingsScreen
import com.devinjapan.aisocialmediaposter.ui.screens.ShareScreen
import com.devinjapan.aisocialmediaposter.ui.theme.AISocialMediaPosterTheme
import com.devinjapan.aisocialmediaposter.ui.utils.BitmapUtils
import com.devinjapan.aisocialmediaposter.ui.utils.ConnectionState
import com.devinjapan.aisocialmediaposter.ui.utils.connectivityState
import com.devinjapan.aisocialmediaposter.ui.viewmodels.CaptionGeneratorViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onStart() {
        super.onStart()
        // Initialize Firebase Auth
        // Should do this with Repository pattern tbh
        auth = Firebase.auth
        // signInIfNecessary(auth)
    }

    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        // askNotificationPermission()

        setContent {
            AISocialMediaPosterTheme {
                // This will cause re-composition on every network state change
                val scaffoldState: ScaffoldState = rememberScaffoldState()
                val connection by connectivityState(applicationContext)

                val isConnected = connection === ConnectionState.Available

                val viewModel = hiltViewModel<CaptionGeneratorViewModel>()
                viewModel.updateConnectionStatus(isConnected)

                Scaffold(scaffoldState = scaffoldState) {
                    if (!isConnected) {
                        LaunchedEffect(Unit) {
                            this.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = getString(R.string.offline),
                                    actionLabel = getString(R.string.dialog_ok_button),
                                    duration = SnackbarDuration.Indefinite
                                )
                            }
                        }
                    }
                    // Show UI when connectivity is available
                    val imageUri = shareImageHandleIntent()
                    Navigation(imageUri, analyticsTracker, viewModel)
                }
            }
        }
    }

    private fun signInIfNecessary(viewModel: CaptionGeneratorViewModel, auth: FirebaseAuth) {
        // Check if user is signed in (non-null) and update UI accordingly.
        // viewModel.signInAnonymouslyIfNecessary()
        // TODO implement this with Repository Pattern and Analytics
    }

    @Suppress("DEPRECATION")
    private fun shareImageHandleIntent(): Uri? {
        if (intent?.action == Intent.ACTION_SEND) {
            if (intent.type?.startsWith("image/") == true) {
                return intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri
            }
        }
        return null
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
fun Navigation(
    imageUri: Uri?,
    analyticsTracker: AnalyticsTracker,
    viewModel: CaptionGeneratorViewModel
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = context.getString(R.string.generator_screen)
    ) {
        composable(context.getString(R.string.generator_screen)) {
            GeneratorScreen(
                navController = navController,
                viewModel = viewModel,
                startingImageUri = imageUri,
                analyticsTracker = analyticsTracker
            )
        }
        composable(context.getString(R.string.share_screen)) {
            ShareScreen(
                navController = navController,
                viewModel = viewModel,
                analyticsTracker = analyticsTracker
            )
        }
        composable(context.getString(R.string.settings_screen)) {
            SettingsScreen(navController = navController)
        }
    }
}

fun preLoadInitialImageAndTags(
    context: Context,
    viewModel: CaptionGeneratorViewModel,
    imageUri: Uri
) {
    val imageBitmap =
        BitmapUtils.getBitmapFromContentUri(context.contentResolver, imageUri)
    if (imageBitmap != null) {
        viewModel.processBitmap(imageBitmap)
    }
}
