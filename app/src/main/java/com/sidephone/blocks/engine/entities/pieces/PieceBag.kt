package com.sidephone.blocks.engine.entities.pieces

class PieceBag {
	private val pieceTypes: List<() -> Piece> = listOf { PieceI() }

	private val pieces: MutableList<Piece> = mutableListOf()


	fun shuffle() {
		val last: Piece? = if (pieces.isNotEmpty()) pieces.last() else null

		pieces.clear()
		pieces.addAll(pieceTypes.map { it() })
		do {
			pieces.shuffle()
		} while (last != null && pieces.first()::class == last::class)
	}


	fun pop(): Piece {
		if (pieces.isEmpty()) {
			shuffle()
		}
		return pieces.removeAt(0)
	}
}
