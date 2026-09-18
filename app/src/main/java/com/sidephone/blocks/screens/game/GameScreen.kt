package com.sidephone.blocks.screens.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.sidephone.blocks.engine.Gameplay
import com.sidephone.blocks.screens.ScreenType


@Composable
fun GameScreen(gameplay: Gameplay, highScore: Int, currentScreen: ScreenType) {
	val hudTextColor = Color.White
	val hudGameOverBackground = Color.Black.copy(alpha = 0.4f)
	val menuBackground = MaterialTheme.colorScheme.background.toArgb()
	val isGameOver by gameplay.gameOver.collectAsState()

	AndroidView(
		modifier = Modifier.fillMaxSize(),
		factory = { context -> GameSurfaceView(context, gameplay, menuBackground) },
	)

	if (currentScreen != ScreenType.Game) {
		return
	}

	if (isGameOver) {
		GameOverOverlay(hudTextColor, hudGameOverBackground)
	}

	HudOverlay(hudTextColor, highScore, gameplay)
}
