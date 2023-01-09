package com.pavelrorecek.todolist.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@Composable
internal fun BaseScreen(
    content: @Composable () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        content()
    }
}
