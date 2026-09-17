package com.sidephone.blocks.engine.entities.pieces

class PieceBag {
	private val pieceTypes: List<Piece> = listOf(
		PieceI(),
		PieceJ(),
		PieceL(),
		PieceO(),
		PieceS(),
		PieceT(),
		PieceZ(),
	)

	private val pieces: MutableList<Piece> = mutableListOf()
	private var last: Piece? = null


	fun shuffle() {
		pieces.clear()
		pieces.addAll(pieceTypes)
		do {
			pieces.shuffle()
		} while (last is Piece && pieces.first()::class == last!!::class)
	}


	fun pop(): Piece {
		if (pieces.isEmpty()) {
			shuffle()
		}

		val piece = pieces.removeAt(0)
		last = piece
		return piece
	}
}
