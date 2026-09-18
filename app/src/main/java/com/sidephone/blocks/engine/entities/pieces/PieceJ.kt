package com.sidephone.blocks.engine.entities.pieces

class PieceJ : Piece() {
	companion object {
		private const val COLOR = 0xFF0000FF.toInt() // blue
	}

	override fun blocks(drawSize: Float): List<Block> {
		return listOf(
			Block(-1, -1, drawSize, COLOR),
			Block(-1, 0, drawSize, COLOR),
			Block(0, 0, drawSize, COLOR),
			Block(1, 0, drawSize, COLOR),
		)
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
}
