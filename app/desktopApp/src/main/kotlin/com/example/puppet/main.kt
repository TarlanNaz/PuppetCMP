package com.example.puppet

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.puppet.resources.Res
import com.example.puppet.resources.app_title
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_title),
    ) {
        App()
    }
}
