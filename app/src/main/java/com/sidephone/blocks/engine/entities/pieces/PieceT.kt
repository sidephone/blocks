package com.sidephone.blocks.engine.entities.pieces

class PieceT : Piece() {
	companion object {
		private const val COLOR = 0xFFFF00FF.toInt() // magenta
	}

	override fun blocks(drawSize: Float): List<Block> {
		return listOf(
			Block(0, -1, drawSize, COLOR),
			Block(-1, 0, drawSize, COLOR),
			Block(0, 0, drawSize, COLOR),
			Block(1, 0, drawSize, COLOR),
		)
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
}
