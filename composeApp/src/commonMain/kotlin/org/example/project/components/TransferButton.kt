package org.example.project.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.FileTransferViewModel
import org.example.project.theme.Actor

@Composable
fun TransferButton(viewModel: FileTransferViewModel) {
    val downloadLink by viewModel.downloadLink.collectAsState()
    Button(
        onClick = {
            if (downloadLink == null) {
                viewModel.uploadFile()
            }
        },
        enabled = !viewModel.isLoading && viewModel.fileContent != null,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF526AFF),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 35.dp),
        contentPadding = PaddingValues(vertical = 15.dp)
    ){
        if (viewModel.isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text(
                if (downloadLink != null) "Uploaded!" else "Create Transfer",
                fontSize = 20.sp,
                fontFamily = Actor(),
                color = Color.White
            )
        }
    }

    viewModel.errorMessage?.let { error ->
        LaunchedEffect(error) {
            viewModel.showNotification("Error")
            viewModel.errorMessage = null
        }
    }

    if (viewModel.downloadLink.value != null) {
        LaunchedEffect(viewModel.downloadLink) {
            viewModel.showNotification("File uploaded")
        }
    }
}