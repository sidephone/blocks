package com.sidephone.blocks.engine.entities.pieces

import com.sidephone.blocks.engine.graphics.DrawCommand

object Block {
	private const val GAP = 1.5f

	fun draw(gridLeft: Int, gridTop: Int, size: Float, color: Int): DrawCommand {
		return DrawCommand.Rect(
			gridLeft * size + GAP,
			gridTop * size + GAP,
			(gridLeft + 1) * size - GAP,
			(gridTop + 1) * size - GAP,
			0f,
			color,
			true
		)
	}
}
