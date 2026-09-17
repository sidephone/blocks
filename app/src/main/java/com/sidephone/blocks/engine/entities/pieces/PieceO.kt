package com.sidephone.blocks.engine.entities.pieces

class PieceO : Piece() {
	companion object {
		private const val COLOR = 0xFFFFFF00.toInt() // yellow
	}

	override fun bottom(orientation: Int) = 2
	override fun left(orientation: Int) = 0
	override fun right(orientation: Int) = 2
	override fun rotateClockwise() {} // this piece does not rotate, so skip unnecessary calculations
	override fun rotateCounterClockwise() {}
	override fun spawnPosition(gridDimensions: Pair<Int, Int>) = Pair(gridDimensions.first / 2 - 1, 0)

	override fun blocks(drawSize: Float): List<Block> {
		return listOf(
			Block(0, 0, drawSize, COLOR),
			Block(1, 0, drawSize, COLOR),
			Block(0, 1, drawSize, COLOR),
			Block(1, 1, drawSize, COLOR)
		)
	}
}
