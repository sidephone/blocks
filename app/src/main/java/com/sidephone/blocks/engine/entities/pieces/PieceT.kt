package com.sidephone.blocks.engine.entities.pieces

import com.sidephone.blocks.engine.graphics.DrawCommand

class PieceT : Piece() {
	companion object {
		private const val COLOR = 0xFFFF00FF.toInt() // magenta
	}

	override fun bottom(orientation: Int): Int {
		return when (orientation) {
			90, 180, 270 -> 2
			else -> 1
		}
	}

	override fun left(orientation: Int): Int {
		return when (orientation) {
			0, 180, 270 -> -1
			else -> 0
		}
	}

	override fun right(orientation: Int): Int {
		return when (orientation) {
			0, 90, 180 -> 2
			else -> 1
		}
	}

	override fun drawBlocks(blockSize: Float): List<DrawCommand> {
		return listOf(
			Block.draw(0, -1, blockSize, COLOR),
			Block.draw(-1, 0, blockSize, COLOR),
			Block.draw(0, 0, blockSize, COLOR),
			Block.draw(1, 0, blockSize, COLOR),
		)
	}
}
