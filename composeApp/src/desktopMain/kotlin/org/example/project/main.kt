package org.example.project

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState(
        width = 400.dp,
        height = 900.dp,
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
        state = state
    ) {
        App()
    }
}