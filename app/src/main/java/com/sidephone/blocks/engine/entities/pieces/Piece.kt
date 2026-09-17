package com.sidephone.blocks.engine.entities.pieces

import android.util.Log
import com.sidephone.blocks.engine.graphics.DrawCommand
import com.sidephone.blocks.engine.graphics.DrawCommandGroup
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToLong

abstract class Piece {
	companion object {
		private val LOG_TAG = Piece::class.simpleName
	}

	protected var blocks = emptyList<Block>()
	private var blockSize = 0f // px
	private var drawOrigin: Pair<Float, Float> = Pair(0f, 0f) // px
	private var isAtTheBottom = false
	private var maxX: Int = 0 // grid cells
	private var maxY: Int = 0 // grid cells
	private var x: Int = 0 // grid cells
	private var y: Int = 0 // grid cells
	protected var orientation: Int = 0 // degrees

	private var lastFallTime = 0L // ms

	private var drawCommands: List<DrawCommand> = emptyList()


	protected abstract fun blocks(drawSize: Float): List<Block>
	protected abstract fun bottom(orientation: Int): Int
	protected abstract fun left(orientation: Int): Int
	protected abstract fun right(orientation: Int): Int


	fun gridX() = x
	fun gridY() = y
	fun isAtTheBottom() = isAtTheBottom
	protected open fun spawnPosition(gridDimensions: Pair<Int, Int>) = Pair(gridDimensions.first / 2 - 1, 1)


	/**
	 * Returns a block list with recalculated grid positions based on the piece orientation.
	 */
	open fun blocks(): List<Block> {
		return blocks.map { block ->
			 when (orientation) {
				90 -> Block(-block.gridY, block.gridX, block.drawSize, block.color, block.shiftX, block.shiftY)
				180 -> Block(-block.gridX, -block.gridY, block.drawSize, block.color, block.shiftX, block.shiftY)
				270 -> Block(block.gridY, -block.gridX, block.drawSize, block.color, block.shiftX, block.shiftY)
				else -> block
			}
		}
	}


	open fun calculateDrawPosition(drawOrigin: Pair<Float, Float>, gridX: Int, gridY: Int, blockSize: Float): Pair<Float, Float> {
		return Pair(
			drawOrigin.first + gridX * blockSize + blockSize / 2,
			drawOrigin.second + gridY * blockSize + blockSize / 2
		)
	}


	fun fall(now: Long, level: Int, heapBlocks: List<Block>) {
		if (isAtTheBottom) return

		if (now - lastFallTime >= fallInterval(level)) {
			moveDown(heapBlocks)
			lastFallTime = now
		}
	}


	/**
	 * Derived from the Nintendo Game Boy specification:
	 * LVL	sec/line
	 * 00    0.8s
	 * 05    0.46s
	 * 10    0.16s
	 * 19    0.03s
	 * 29+   0.016s
	 *
	 * The approximation is: 16 + 785 * e^(-(level / 7.2)^1.46)
	 */
	fun fallInterval(level: Int): Long {
		val saneLevel: Double = (if (level < 0) 0 else level).toDouble()
		val dt = 16 + 785 * exp(-((saneLevel / 7.2).pow(1.46)))
		return dt.roundToLong().coerceAtLeast(16L)
	}


	fun moveDown(heapBlocks: List<Block>) {
		if (y + bottom(orientation) >= maxY) {
			isAtTheBottom = true
			return
		}

		for (block in blocks()) {
			for (otherBlock in heapBlocks) {
				if (block.gridX + x == otherBlock.gridX && block.gridY + y + 1 == otherBlock.gridY) {
					isAtTheBottom = true
					return
				}
			}
		}

		y += 1
	}


	fun moveLeft() {
		if (!isAtTheBottom && x + left(orientation) > 0) x -= 1
	}


	fun moveRight() {
		if (!isAtTheBottom && x + right(orientation) < maxX) x += 1
	}


	open fun rotateClockwise() {
		if (isAtTheBottom) return

		orientation += 90
		orientation %= 360
		wallKick()

		Log.d(LOG_TAG, "position: ($x, $y) orientation: $orientation")
	}


	open fun rotateCounterClockwise() {
		if (isAtTheBottom) return

		orientation -= 90
		orientation = if (orientation < 0) orientation + 360 else orientation
		wallKick()

		Log.d(LOG_TAG, "position: ($x, $y) orientation: $orientation")
	}


	fun draw(): DrawCommandGroup {
		val (drawX, drawY) = calculateDrawPosition(drawOrigin, x, y, blockSize)
		return DrawCommandGroup(drawX,drawY,orientation.toFloat(), drawCommands)
	}


	fun spawn(now: Long, gridPosition: Pair<Float, Float>, gridDimensions: Pair<Int, Int>, gridCellSize: Float) {
		blockSize = gridCellSize
		drawOrigin = gridPosition
		isAtTheBottom = false
		maxX = gridDimensions.first
		maxY = gridDimensions.second
		orientation = 0
		spawnPosition(gridDimensions).let {
			x = it.first
			y = it.second
		}

		blocks = blocks(blockSize)
		drawCommands = blocks.map { it.draw() }

		lastFallTime = now
	}


	private fun wallKick() {
		if (x + right(orientation) > maxX) {
			x = maxX - right(orientation)
		}

		if (x + left(orientation) < 0) {
			x = abs(left(orientation))
		}

		if (y + bottom(orientation) > maxY) {
			y = maxY - bottom(orientation)
		}
	}
}
