package com.sidephone.blocks.engine.entities

/**
 * This represents the background of the game. Currently, it is just a solid color, but you can
 * add stars and other elements to make it more interesting. To do this, create a function to
 * generate DrawCommand objects, similar to the Ship class, and add them to the GameFrame in the
 * Gameplay.render()
 */
object Space {
	const val BACKGROUND = 0xFF000000.toInt()
}
