package com.sidephone.blocks.screens.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.sidephone.blocks.R
import com.sidephone.blocks.engine.Gameplay
import com.sidephone.blocks.ui.theme.Dimens

@Composable
fun HudOverlay(textColor: Color, highScore: Int, gameplay: Gameplay) {
	val level by gameplay.level.collectAsState()
	val lines by gameplay.lines.collectAsState()
	val score by gameplay.score.collectAsState()
	val (x, y) = gameplay.scoreboardPosition()

	Box(modifier = Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier
				.width(with(LocalDensity.current) { gameplay.scoreboardWidth().toDp() } )
				.offset { IntOffset(x = x.toInt(), y = y.toInt()) }
				.padding(Dimens.HudPadding)
		) {
			ScoreboardRow(titleResId = R.string.scoreboard_score, value = score, textColor = textColor)
			ScoreboardRow(titleResId = R.string.scoreboard_level, value = level, textColor = textColor)
			ScoreboardRow(titleResId = R.string.scoreboard_lines, value = lines, textColor = textColor)
		}
	}
}
