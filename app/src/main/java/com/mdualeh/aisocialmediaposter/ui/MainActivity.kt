package com.mdualeh.aisocialmediaposter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mdualeh.aisocialmediaposter.R
import com.mdualeh.aisocialmediaposter.ui.screens.GeneratorScreen
import com.mdualeh.aisocialmediaposter.ui.screens.ShareScreen
import com.mdualeh.aisocialmediaposter.ui.theme.AISocialMediaPosterTheme
import com.mdualeh.aisocialmediaposter.ui.viewmodels.TextCompletionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AISocialMediaPosterTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    val viewModel = hiltViewModel<TextCompletionViewModel>()

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
