package com.sidephone.blocks.engine.entities.pieces

class PieceI : Piece() {
	companion object {
		private const val COLOR = 0xFF00FFFF.toInt() // cyan
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

	override fun blocks(drawSize: Float): List<Block> {
		return listOf(
			Block(-2, -1, drawSize, COLOR, 0.5f, 0.5f),
			Block(-1, -1, drawSize, COLOR, 0.5f, 0.5f),
			Block(0, -1, drawSize, COLOR, 0.5f, 0.5f),
			Block(1, -1, drawSize, COLOR, 0.5f, 0.5f),
		)
	}

	override fun calculateDrawPosition(drawOrigin: Pair<Float, Float>, gridX: Int, gridY: Int, blockSize: Float): Pair<Float, Float> {
		return Pair(
			drawOrigin.first + gridX * blockSize,
			drawOrigin.second + gridY * blockSize
		)
	}

	override fun spawnPosition(gridDimensions: Pair<Int, Int>): Pair<Int, Int> {
		return Pair(gridDimensions.first / 2, 1)
	}
}
