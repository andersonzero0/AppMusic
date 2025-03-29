package com.andersonzero0.appmusic.ui.components.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Screen(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box (modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        content()
    }
}

@Preview
@Composable
fun ScreenPreview() {
    Screen {}
}