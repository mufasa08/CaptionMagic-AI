package com.mdualeh.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.util.Pair
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mdualeh.aisocialmediaposter.ui.components.ImagePicker
import com.mdualeh.aisocialmediaposter.ui.utils.BitmapUtils
import com.mdualeh.aisocialmediaposter.ui.viewmodels.TextCompletionViewModel
import java.io.IOException

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
        // Text(text = "${viewModel.state.textCompletion?.choices?.first()?.text}", modifier = Modifier.padding(6.dp))
        // viewModel.testGeneratorApi()
        ImagePicker(viewModel)
    }
}
