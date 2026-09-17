package com.sidephone.blocks.engine.entities.pieces

import com.sidephone.blocks.engine.graphics.DrawCommand

class Block(var gridX: Int, var gridY: Int, val drawSize: Float, val color: Int, val shiftX: Float = 0f, val shiftY: Float = 0f) {
	companion object {
		private const val GAP = 1.5f
	}


	fun draw() = drawAt(Pair(0f, 0f))


	fun drawAt(position: Pair<Float, Float>): DrawCommand {
		return DrawCommand.Rect(
			position.first + (-0.5f + shiftX + gridX) * drawSize + GAP,
			position.second + (-0.5f + shiftY + gridY) * drawSize + GAP,
			position.first + (0.5f + shiftX + gridX) * drawSize - GAP,
			position.second + (0.5f + shiftY + gridY) * drawSize - GAP,
			0f,
			color,
			true
		)
	}


	fun moveBy(dx: Int, dy: Int): Block {
		return Block(gridX + dx, gridY + dy, drawSize, color, shiftX, shiftY)
	}
}
