package com.sidephone.blocks.settings

import androidx.core.content.edit

class Settings(context: android.content.Context) {
	companion object {
		private const val PREFS_NAME = "SpaceBlasterSettings"
		private const val HIGH_SCORE_KEY = "hs"
	}

	object Engine {
		// faster loop to handle fastest piece drop speed properly
		const val TARGET_FPS = 120
		const val TARGET_IPS = 120
	}

	object Gameplay {
		const val LINES_PER_LEVEL = 10
	}

	private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)


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
