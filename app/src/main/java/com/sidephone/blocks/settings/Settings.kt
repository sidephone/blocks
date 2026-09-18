package com.sidephone.blocks.settings

import androidx.core.content.edit
import kotlin.math.min

class Settings(context: android.content.Context) {
	companion object {
		private const val PREFS_NAME = "SpaceBlasterSettings"
		private const val GHOST_PIECE_KEY = "ghost_piece"
		private const val HIGH_SCORE_KEY = "hs"
	}

	object Engine {
		// faster loop to handle fastest piece drop speed properly
		const val TARGET_FPS = 120
		const val TARGET_IPS = 120
		const val MOVE_KEY_REPEAT_WAIT_TIME = 250L // ms
		val MOVE_KEY_REPEAT_INTERVAL = 1000L / min(10, TARGET_IPS) // 10 times per second but not faster than the engine can handle
	}

	object Gameplay {
		const val LINES_PER_LEVEL = 10
		const val POINTS_PER_1_LINE = 2
		const val POINTS_PER_2_LINES = 5
		const val POINTS_PER_3_LINES = 15
		const val POINTS_PER_4_LINES = 60
	}

	private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)


	fun ghostPiece(): Boolean {
		return sharedPreferences.getBoolean(GHOST_PIECE_KEY, true)
	}

	fun setGhostPiece(enabled: Boolean) {
		sharedPreferences.edit { putBoolean(GHOST_PIECE_KEY, enabled) }
	}


	fun getHighScore(): Int {
		return sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
	}


	fun updateHighScoreIfNeeded(newScore: Int): Boolean {
		val currentHighScore = getHighScore()
		if (newScore > currentHighScore) {
			sharedPreferences.edit { putInt(HIGH_SCORE_KEY, newScore) }
			return true
		}

		return false
	}
}
