package com.sidephone.blocks.engine.entities.pieces

import com.sidephone.blocks.engine.graphics.DrawCommand

class Block(val gridX: Int, val gridY: Int, val drawSize: Float, val color: Int, val shiftX: Float = 0f, val shiftY: Float = 0f) {
	companion object {
		private const val GAP = 1.5f
	}


	fun draw(): DrawCommand {
		return DrawCommand.Rect(
			(-0.5f + shiftX + gridX) * drawSize + GAP,
			(-0.5f + shiftY + gridY) * drawSize + GAP,
			(0.5f + shiftX + gridX) * drawSize - GAP,
			(0.5f + shiftY + gridY) * drawSize - GAP,
			0f,
			color,
			true
		)
	}
}
