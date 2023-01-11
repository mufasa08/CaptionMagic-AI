package com.mdualeh.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mdualeh.aisocialmediaposter.ui.viewmodels.TextCompletionViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GeneratorScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<TextCompletionViewModel>()
    viewModel.testGeneratorApi()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${viewModel.state.textCompletion?.choices?.first()?.text}", modifier = Modifier.padding(6.dp))
        }
    }
}

/*@Composable
fun CheckLoginState(viewModel: LoginViewModel, navController: NavController, context: Context) {
    val loginState = viewModel.authState
    when (loginState.value) {
        is AuthenticationState.Authenticated -> {
            LaunchedEffect(Unit) {
                navController.navigate(context.getString(R.string.pickup_service_screen))
            }
        }
        is AuthenticationState.SignedOut -> {
            println("Signed Out")
        }
        is AuthenticationState.Failure -> {
            println((loginState.value as AuthenticationState.Failure).failureMessage)
        }
        is AuthenticationState.Loading -> {
            LoadingIndicatorView()
        }
    }
}*/
