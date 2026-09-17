package com.sidephone.blocks.engine.entities.pieces

import com.sidephone.blocks.engine.graphics.DrawCommand

object Block {
	private const val GAP = 1.5f


	fun draw(gridLeft: Int, gridTop: Int, size: Float, color: Int): DrawCommand {
		return drawShifted(gridLeft, gridTop, size, color, 0f, 0f)
	}


	fun drawShifted(gridLeft: Int, gridTop: Int, size: Float, color: Int, shiftX: Float, shiftY: Float): DrawCommand {
		return DrawCommand.Rect(
			(-0.5f + shiftX + gridLeft) * size + GAP,
			(-0.5f + shiftY + gridTop) * size + GAP,
			(0.5f + shiftX + gridLeft) * size - GAP,
			(0.5f + shiftY + gridTop) * size - GAP,
			0f,
			color,
			true
		)
	}
}
