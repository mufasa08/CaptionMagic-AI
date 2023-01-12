package com.mdualeh.aisocialmediaposter.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.mdualeh.aisocialmediaposter.R
import com.mdualeh.aisocialmediaposter.ui.components.ImagePicker
import com.mdualeh.aisocialmediaposter.ui.viewmodels.TextCompletionViewModel
import org.compose.museum.simpletags.SimpleTags

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GeneratorScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<TextCompletionViewModel>()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                elevation = 0.dp,
                title = {
                    Text(context.getString(R.string.app_name), style = MaterialTheme.typography.h6)
                },
                backgroundColor = MaterialTheme.colors.surface,
            )
            ImagePicker(modifier = Modifier.height(400.dp).fillMaxWidth(), viewModel)
            ListOfTags(list = viewModel.state.loadedTags)
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    // generate post
                },
            ) {
                Text(
                    text = "Generate Post"
                )
            }
        }
    }
}
@Composable
fun ListOfTags(list: List<String>) {
    FlowRow {
        list.forEach { tag ->
            SimpleTags(modifier = Modifier.wrapContentWidth().padding(4.dp), text = tag, onClick = {
            })
        }
    }
}
