package com.devinjapan.aisocialmediaposter.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.devinjapan.aisocialmediaposter.R

@Composable
fun ErrorDialog(
    errorTitle: String = "Error",
    errorMessage: String,
    onConfirmClick: () -> Unit = {}
) {
    val context = LocalContext.current
    AlertDialog(
        modifier = Modifier.widthIn(min = 400.dp),
        onDismissRequest = {},
        title = { Text(text = errorTitle) },
        text = { Text(errorMessage) },
        confirmButton = {
            Button(
                onClick = { onConfirmClick() }
            ) {
                Text(context.getString(R.string.dialog_ok_button))
            }
        }
    )
}
