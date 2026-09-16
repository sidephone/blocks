package com.sidephone.blocks.settings

import androidx.core.content.edit

class Settings(context: android.content.Context) {
	companion object {
		private const val PREFS_NAME = "SpaceBlasterSettings"
		private const val HIGH_SCORE_KEY = "hs"
	}

	object Gameplay {
		const val TARGET_FPS = 15
		const val TARGET_IPS = 15
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
