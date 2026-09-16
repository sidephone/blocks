package com.sidephone.blocks.engine.entities.pieces

import com.sidephone.blocks.engine.entities.Playground
import com.sidephone.blocks.engine.graphics.DrawCommand

class PieceI : Piece() {
	object Color {
		const val FILL = 0xFF00FFFF.toInt() // Cyan
		const val OUTLINE = Playground.BACKGROUND
	}

	override fun drawBlocks(): List<DrawCommand> {
		return listOf(
			// fill color
//			DrawCommand.Rect(-2 * blockSize, -2 * blockSize, 2 * blockSize, 2 * blockSize, 0f, Color.FILL, false),

			DrawCommand.Rect(-2 * blockSize, -blockSize, -blockSize, 0f, 0f, Color.FILL, false),
			DrawCommand.Rect(-2 * blockSize, -blockSize, 0f, 0f, 0f, Color.FILL, false),
			DrawCommand.Rect(-2 * blockSize, -blockSize, blockSize, 0f, 0f, Color.FILL, false),
			DrawCommand.Rect(-2 * blockSize, -blockSize, 2 * blockSize, 0f, 0f, Color.FILL, false),
		)
	}


	override fun bottom(): Int {
		return when (orientation) {
			0 -> 0
			90, 270 -> 2
			180 -> 1
			else -> 0
		}
	}


	override fun left(): Int {
		return when (orientation) {
			0, 180 -> -2
			90 -> 0
			270 -> -1
			else -> 0
		}
	}

	override fun right(): Int {
		return when (orientation) {
			0, 180 -> 2
			90 -> 1
			270 -> 0
			else -> 0
		}
	}


	override fun spawnPosition(gridDimensions: Pair<Int, Int>): Pair<Int, Int> {
		return Pair(gridDimensions.first / 2, 1)
	}
}
