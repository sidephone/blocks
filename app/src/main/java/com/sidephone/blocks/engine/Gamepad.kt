package com.sidephone.blocks.engine

import android.view.KeyCharacterMap
import android.view.KeyEvent


/**
 * Represents the gamepad controller. It takes input from any Activity.onKeyDown, Activity.onKeyUp,
 * then stores the pressed keys in a set, to be used by other game components. The keys are represented
 * by their KeyEvent key codes, and remain in the set for as long as the user holds the button down.
 * When the button is released, the key is removed from the set.
 * This class already supports all gamepad buttons, so you should not need to modify it.
 */
class Gamepad {
	val pressedKeys = mutableSetOf<Int>()


	fun isSundial(): Boolean {
		return KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS) && KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_MEDIA_NEXT) && KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
	}


	fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
		if (hasKey(keyCode) && event?.repeatCount == 0) {
			pressedKeys.add(keyCode)
			return true
		}

		return false
	}


	fun onKeyUp(keyCode: Int): Boolean {
		if (hasKey(keyCode)) {
			pressedKeys.remove(keyCode)
			return true
		}

		return false
	}


	fun reset() {
		pressedKeys.clear()
	}


	private fun hasKey(keyCode: Int): Boolean {
		return when (keyCode) {
			// QWERTY
			KeyEvent.KEYCODE_Q,
			KeyEvent.KEYCODE_T,
			KeyEvent.KEYCODE_O,
			KeyEvent.KEYCODE_D,
			KeyEvent.KEYCODE_J,
			KeyEvent.KEYCODE_B,
			KeyEvent.KEYCODE_SPACE,
			KeyEvent.KEYCODE_ENTER,

			// T9
			KeyEvent.KEYCODE_1,
			KeyEvent.KEYCODE_2,
			KeyEvent.KEYCODE_3,
			KeyEvent.KEYCODE_4,
			KeyEvent.KEYCODE_6,
			KeyEvent.KEYCODE_8,
			KeyEvent.KEYCODE_0,
			KeyEvent.KEYCODE_DPAD_CENTER,

			// Sundial
			// KEYCODE_DPAD_LEFT must be remapped to KEYCODE_BUTTON_A
			// KEYCODE_DPAD_RIGHT must be remapped to KEYCODE_BUTTON_B
			KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, // enter
			KeyEvent.KEYCODE_TAB, // bottom left
			KeyEvent.KEYCODE_MEDIA_NEXT, // right
			KeyEvent.KEYCODE_MEDIA_PREVIOUS, // left

			// Gamepad
			KeyEvent.KEYCODE_BUTTON_A,
			KeyEvent.KEYCODE_BUTTON_B,
			KeyEvent.KEYCODE_BUTTON_X,
			KeyEvent.KEYCODE_BUTTON_Y,
			KeyEvent.KEYCODE_BUTTON_SELECT,
			KeyEvent.KEYCODE_BUTTON_START,
			KeyEvent.KEYCODE_DPAD_UP,
			KeyEvent.KEYCODE_DPAD_DOWN,
			KeyEvent.KEYCODE_DPAD_LEFT,
			KeyEvent.KEYCODE_DPAD_RIGHT -> true
			else -> false
		}
	}
}
