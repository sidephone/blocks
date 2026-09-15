package com.sidephone.blocks.screens.game

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ScoreboardRow(titleResId: Int, value: Int, textColor: Color) {
	Text(
		text = stringResource(titleResId),
		style = typography.bodyLarge,
		color = textColor,
		textAlign = TextAlign.Center,
		modifier = Modifier.fillMaxWidth()
	)

	Text(
		text = "$value",
		style = typography.bodyLarge,
		color = textColor,
		textAlign = TextAlign.Right,
		modifier = Modifier.fillMaxWidth()
	)
}
