package com.sidephone.blocks.engine.entities.pieces

import android.util.Log
import com.sidephone.blocks.engine.graphics.DrawCommand
import com.sidephone.blocks.engine.graphics.DrawCommandGroup
import kotlin.math.abs

abstract class Piece {
	companion object {
		private val LOG_TAG = Piece::class.simpleName
	}

	protected var blockSize = 0f
	private var drawOrigin: Pair<Float, Float> = Pair(0f, 0f)
	private var maxX: Int = 0
	private var maxY: Int = 0
	private var x: Int = 0
	private var y: Int = 0
	protected var orientation: Int = 0


	protected abstract fun drawBlocks(): List<DrawCommand>
	protected abstract fun bottom(): Int
	protected abstract fun left(): Int
	protected abstract fun right(): Int
	protected abstract fun spawnPosition(gridDimensions: Pair<Int, Int>): Pair<Int, Int>


	fun calculateDrawPosition(): Pair<Float, Float> {
		return Pair(
			drawOrigin.first + x * blockSize,
			drawOrigin.second + y * blockSize
		)
	}


	fun moveDown() {
		if (y < maxY - 1) y += 1
	}


	fun moveLeft() {
		if (x + left() > 0) x -= 1
	}


	fun moveRight() {
		if (x + right() < maxX) x += 1
	}


	fun rotateClockwise() {
		orientation -= 90
		orientation = if (orientation < 0) orientation + 360 else orientation
		wallKick()

		Log.d(LOG_TAG, "position: ($x, $y) orientation: $orientation")
	}


	fun rotateCounterClockwise() {
		orientation += 90
		orientation %= 360
		wallKick()

		Log.d(LOG_TAG, "position: ($x, $y) orientation: $orientation")
	}


	fun draw(): DrawCommandGroup {
		val (x, y) = calculateDrawPosition()
		return DrawCommandGroup(x, y, orientation.toFloat(), drawBlocks())
	}


	fun spawn(now: Long, gridPosition: Pair<Float, Float>, gridDimensions: Pair<Int, Int>, gridCellSize: Float) {
		blockSize = gridCellSize
		drawOrigin = gridPosition
		maxX = gridDimensions.first
		maxY = gridDimensions.second
		spawnPosition(gridDimensions).let {
			x = it.first
			y = it.second
		}
	}


	private fun wallKick() {
		if (x + right() > maxX) {
			x = maxX - right()
		}

		if (x + left() < 0) {
			x = abs(left())
		}
	}
}
