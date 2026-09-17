package com.sidephone.blocks.engine.entities.pieces

import com.sidephone.blocks.engine.graphics.DrawCommand

class PieceI : Piece() {
	companion object {
		private const val COLOR = 0xFF00FFFF.toInt() // Cyan
	}


	override fun drawBlocks(blockSize: Float): List<DrawCommand> {
		// The coordinates are in a local coordinate system, defined by the piece DrawCommandGroup.
		// The unit is a grid cell, not a pixel.
		return listOf(
			Block.draw(-2, -1, blockSize, COLOR),
			Block.draw(-1, -1, blockSize, COLOR),
			Block.draw(0, -1, blockSize, COLOR),
			Block.draw(1, -1, blockSize, COLOR),
		)
	}


	override fun bottom(orientation: Int): Int {
		return when (orientation) {
			0 -> 0
			90, 270 -> 2
			180 -> 1
			else -> 0
		}
	}


	override fun left(orientation: Int): Int {
		return when (orientation) {
			0, 180 -> -2
			90 -> 0
			270 -> -1
			else -> 0
		}
	}

	override fun right(orientation: Int): Int {
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
