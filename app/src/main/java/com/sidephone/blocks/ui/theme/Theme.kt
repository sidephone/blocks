package com.sidephone.blocks.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun GameTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	// dynamic colors seem to be causing discrepancies on our devices, so we stick with a fixed
	// color scheme for now
	dynamicColor: Boolean = false,
	content: @Composable () -> Unit
) {
	MaterialTheme(
		colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
		typography = Typography,
		content = content
	)
}
