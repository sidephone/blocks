package com.sidephone.blocks.engine

import com.sidephone.blocks.engine.entities.pieces.Piece
import com.sidephone.blocks.engine.graphics.DrawCommandGroup

class GhostPiece {
	var pieceType: Class<out Piece>? = null
	var pieceX = -1
	var orientation = 0
	var y = 0


	fun clear() {
		pieceType = null
		pieceX = -1
		orientation = 0
		y = -1
	}


	/**
	 * Determines where the current piece would land if it were to drop straight down,
	 * accounting for each column of the piece independently (so uneven bottoms — T, L,
	 * J, S, Z — land correctly against uneven heap surfaces).
	 */
	private fun determineY(piece: Piece, heapPeaks: List<Int>): Int {
		if (
			pieceType == piece::class.java
			&& pieceX == piece.gridX()
			&& orientation == piece.orientation()
			&& y > 0
		) {
			return y
		}

		pieceType = piece::class.java
		pieceX = piece.gridX()
		orientation = piece.orientation()

		// For each column the piece occupies, find its own lowest block (max absolute Y) —
		// that's the block which will actually collide first as the piece falls.
		val lowestYPerColumn = piece.blockGridPositions()
			.groupBy { (blockX, _) -> blockX }
			.mapValues { (_, blocksInColumn) -> blocksInColumn.maxOf { (_, blockY) -> blockY } }

		// The piece can fall until the most restrictive column runs out of room.
		var maxDrop = Int.MAX_VALUE
		for ((col, blockAbsY) in lowestYPerColumn) {
			val ceilingY = heapPeaks.getOrElse(col) { Int.MAX_VALUE } // empty/out-of-range column: no obstruction above the floor
			val allowedDrop = (ceilingY - 1) - blockAbsY
			if (allowedDrop < maxDrop) maxDrop = allowedDrop
		}

		if (maxDrop < 0) maxDrop = 0
		y = piece.gridY() + maxDrop
		return y
	}


	fun draw(piece: Piece, heapPeaks: List<Int>): DrawCommandGroup? {
		val ghostGridY = determineY(piece, heapPeaks)
		if (ghostGridY == piece.gridY()) {
			return null
		}
		return piece.drawGhost(ghostGridY)
	}
}
