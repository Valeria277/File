package org.example.project.utils

import androidx.compose.runtime.Composable
import javax.swing.JFileChooser

@Composable
actual fun UploadFile(
    onFileUploaded: (ByteArray, String) -> Unit,
    onError: (Throwable) -> Unit
) {
    try {
        val fileChooser = JFileChooser().apply {
            dialogTitle = "Выберите файл для загрузки"
            isAcceptAllFileFilterUsed = true
        }

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            val bytes = file.readBytes()
            onFileUploaded(bytes, file.name)
        }
    } catch (e: Exception) {
        onError(e)
    }
}