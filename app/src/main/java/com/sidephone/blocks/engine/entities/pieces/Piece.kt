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

	protected var blockSize = 0f
	private var drawOrigin: Pair<Float, Float> = Pair(0f, 0f)
	private var isAtTheBottom = false
	private var maxX: Int = 0
	private var maxY: Int = 0
	private var x: Int = 0
	private var y: Int = 0
	protected var orientation: Int = 0

	private var lastFallTime = 0L


	protected abstract fun drawBlocks(): List<DrawCommand>
	protected abstract fun bottom(): Int
	protected abstract fun left(): Int
	protected abstract fun right(): Int
	protected abstract fun spawnPosition(gridDimensions: Pair<Int, Int>): Pair<Int, Int>

	fun isAtTheBottom() = isAtTheBottom


	fun calculateDrawPosition(): Pair<Float, Float> {
		return Pair(
			drawOrigin.first + x * blockSize,
			drawOrigin.second + y * blockSize
		)
	}


	fun fall(now: Long, level: Int) {
		if (isAtTheBottom) return

		if (now - lastFallTime >= fallInterval(level)) {
			moveDown()
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


	fun moveDown() {
		if (y + bottom() < maxY)
			y += 1
		else
			isAtTheBottom = true
	}


	fun moveLeft() {
		if (!isAtTheBottom && x + left() > 0) x -= 1
	}


	fun moveRight() {
		if (!isAtTheBottom && x + right() < maxX) x += 1
	}


	fun rotateClockwise() {
		if (isAtTheBottom) return

		orientation -= 90
		orientation = if (orientation < 0) orientation + 360 else orientation
		wallKick()

		Log.d(LOG_TAG, "position: ($x, $y) orientation: $orientation")
	}


	fun rotateCounterClockwise() {
		if (isAtTheBottom) return

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
		isAtTheBottom = false
		maxX = gridDimensions.first
		maxY = gridDimensions.second
		spawnPosition(gridDimensions).let {
			x = it.first
			y = it.second
		}

		lastFallTime = now
	}


	private fun wallKick() {
		if (x + right() > maxX) {
			x = maxX - right()
		}

		if (x + left() < 0) {
			x = abs(left())
		}

		if (y + bottom() >= maxY) {
			y = maxY - bottom() - 1
		}
	}
}
