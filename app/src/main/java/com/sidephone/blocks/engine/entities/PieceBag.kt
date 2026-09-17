package com.sidephone.blocks.engine.entities

import com.sidephone.blocks.engine.entities.pieces.Piece
import com.sidephone.blocks.engine.entities.pieces.PieceI
import com.sidephone.blocks.engine.entities.pieces.PieceJ
import com.sidephone.blocks.engine.entities.pieces.PieceL
import com.sidephone.blocks.engine.entities.pieces.PieceO
import com.sidephone.blocks.engine.entities.pieces.PieceS
import com.sidephone.blocks.engine.entities.pieces.PieceT
import com.sidephone.blocks.engine.entities.pieces.PieceZ

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
