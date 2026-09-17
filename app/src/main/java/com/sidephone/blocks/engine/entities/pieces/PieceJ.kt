package com.sidephone.blocks.engine.entities.pieces

import com.sidephone.blocks.engine.graphics.DrawCommand

class PieceJ : Piece() {
	companion object {
		private const val COLOR = 0xFF0000FF.toInt() // blue
	}

	override fun bottom(orientation: Int) = when (orientation) {
		0 -> 1
		else -> 2
	}

	override fun left(orientation: Int) = when (orientation) {
		0, 180, 270 -> -1
		else -> 0
	}

	override fun right(orientation: Int) = when (orientation) {
		0, 90, 180 -> 2
		else -> 1
	}

	override fun drawBlocks(blockSize: Float): List<DrawCommand> {
		return listOf(
			Block.draw(-1, -1, blockSize, COLOR),
			Block.draw(-1, 0, blockSize, COLOR),
			Block.draw(0, 0, blockSize, COLOR),
			Block.draw(1, 0, blockSize, COLOR),
		)
	}
}
