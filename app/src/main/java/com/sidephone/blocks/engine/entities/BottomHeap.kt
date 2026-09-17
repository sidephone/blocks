package com.sidephone.blocks.engine.entities

import com.sidephone.blocks.engine.entities.pieces.Block
import com.sidephone.blocks.engine.entities.pieces.Piece
import com.sidephone.blocks.engine.graphics.DrawCommand
import com.sidephone.blocks.engine.graphics.DrawCommandGroup

/**
 * Represents the heap of blocks at the bottom of the game area.
 */
class BottomHeap {
	private var cellSize = 0f
	private val drawCommands = mutableMapOf<Int, MutableList<DrawCommand>>()
	private val lines = mutableMapOf<Int, MutableList<Block>>() // key: lineY, value: list of blocks in that line
	private var maxBlocksPerLine = 0
	private var maxLines = 0


	fun getBlocks() = lines.values.flatten()


	fun add(piece: Piece) {
		piece.blocks().forEach { block -> run {
			val newBlock = block.moveBy(piece.gridX(), piece.gridY())

			lines.getOrPut(newBlock.gridY) { mutableListOf() }
				.add(newBlock)

			val drawPosition = piece.calculateDrawPosition(Pair(0f, 0f), piece.gridX(), piece.gridY(), block.drawSize)

			drawCommands.getOrPut(newBlock.gridY) { mutableListOf() }
				.add(block.drawAt(drawPosition))
		}}
	}


	fun clearCompleteLines(): List<Int> {
		val linesToClear = lines.filter { it.value.size >= maxBlocksPerLine }.keys.sortedDescending()
		if (linesToClear.isEmpty()) return emptyList()
		linesToClear.forEach { lineY ->
			lines.remove(lineY)
			drawCommands.remove(lineY)
		}
		return linesToClear
	}


	fun draw(playgroundPosition: Pair<Float, Float>): DrawCommandGroup {
		return DrawCommandGroup(
			playgroundPosition.first,
			playgroundPosition.second,
			0f,
			drawCommands.values.flatten()
		)
	}


	fun moveDownLines(removedLines: List<Int>) {
		if (removedLines.isEmpty()) return

		val clearedSorted = removedLines.sorted() // ascending

		// Any remaining line that has at least one cleared line below it (greater Y)
		// needs to shift down by however many cleared lines are below it.
		val affectedLineYs = lines.keys
			.filter { lineY -> clearedSorted.any { it > lineY } }
			.sortedDescending() // process bottom-most affected line first

		for (lineY in affectedLineYs) {
			val shift = clearedSorted.count { it > lineY }
			if (shift == 0) continue

			val newY = lineY + shift
			val blocksToMoveDown = lines.remove(lineY) ?: mutableListOf()
			val drawCommandsToMoveDown = drawCommands.remove(lineY) ?: mutableListOf()

			lines[newY] = blocksToMoveDown
				.map { it.moveBy(0, shift) }
				.toMutableList()

			drawCommands[newY] = drawCommandsToMoveDown
				.map { (it as DrawCommand.Rect).translatedRect(0f, cellSize * shift, it) }
				.toMutableList()
		}
	}


	fun reset(playgroundDimensions: Pair<Int, Int>, playgroundCellSize: Float) {
		cellSize = playgroundCellSize
		drawCommands.clear()
		lines.clear()
		maxBlocksPerLine = playgroundDimensions.first
		maxLines = playgroundDimensions.second
	}
}
