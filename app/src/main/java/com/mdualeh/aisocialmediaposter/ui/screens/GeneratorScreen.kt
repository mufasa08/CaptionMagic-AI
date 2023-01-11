package com.mdualeh.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mdualeh.aisocialmediaposter.ui.viewmodels.TextCompletionViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GeneratorScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<TextCompletionViewModel>()


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
            Button(
                onClick = {
                    viewModel.testGeneratorApi()
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                /*Image(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = ""
                )*/
                Text(text = "LOAD", modifier = Modifier.padding(6.dp))
            }
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
