package com.devinjapan.aisocialmediaposter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.*
import com.devinjapan.aisocialmediaposter.R

@Composable
fun GeneratingDialog() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.magician))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Dialog(
        onDismissRequest = {
        }
    ) {
        Column(
            Modifier
                .background(color = Color.Transparent)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            )
        }
    }
}
