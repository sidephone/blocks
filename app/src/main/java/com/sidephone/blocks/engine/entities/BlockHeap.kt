package com.sidephone.blocks.engine.entities

import com.sidephone.blocks.engine.entities.pieces.Block
import com.sidephone.blocks.engine.entities.pieces.Piece
import com.sidephone.blocks.engine.graphics.DrawCommand
import com.sidephone.blocks.engine.graphics.DrawCommandGroup

/**
 * Represents the heap of blocks at the bottom of the game area.
 */
class BlockHeap {
	private val drawCommands = mutableListOf<DrawCommand>()
	private val blocks = mutableListOf<Block>()


	fun add(piece: Piece) {
		piece.blocks().forEach { block ->
			blocks.add(block.moveBy(piece.gridX(), piece.gridY()))
			drawCommands.add(block.drawAt(
				piece.calculateDrawPosition(Pair(0f, 0f), piece.gridX(), piece.gridY(), block.drawSize)
			))
		}
	}


	fun clear() {
		blocks.clear()
		drawCommands.clear()
	}


	fun draw(playgroundPosition: Pair<Float, Float>): DrawCommandGroup {
		return DrawCommandGroup(
			playgroundPosition.first,
			playgroundPosition.second,
			0f,
			drawCommands
		)
	}


	fun getBlocks(): List<Block> {
		return blocks
	}
}
