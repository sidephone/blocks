package com.sidephone.blocks.engine.entities.pieces

import com.sidephone.blocks.engine.graphics.DrawCommand

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

	override fun drawBlocks(blockSize: Float): List<DrawCommand> {
		return listOf(
			Block.draw(0, 0, blockSize, COLOR),
			Block.draw(1, 0, blockSize, COLOR),
			Block.draw(0, 1, blockSize, COLOR),
			Block.draw(1, 1, blockSize, COLOR)
		)
	}
}
