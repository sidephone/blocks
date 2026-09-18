package com.sidephone.blocks.engine

import com.sidephone.blocks.engine.entities.pieces.Piece
import com.sidephone.blocks.engine.entities.pieces.PieceO
import com.sidephone.blocks.engine.graphics.DrawCommandGroup

class PreviewWindow {
	companion object {
		fun drawNextPiece(next: Piece, previewCenter: Pair<Float, Float>, blockSize: Float): DrawCommandGroup {
			var (x, y) = previewCenter
			if (next is PieceO) {
				y -= blockSize / 2
				x -= blockSize / 2
			} else {
				y += blockSize / 2
			}

			return next.drawPreview(Pair(x, y), blockSize)
		}
	}
}
