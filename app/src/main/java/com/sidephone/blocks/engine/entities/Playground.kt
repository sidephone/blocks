package com.sidephone.blocks.engine.entities

import com.sidephone.blocks.engine.graphics.DrawCommand
import com.sidephone.blocks.engine.graphics.DrawCommandGroup


class Playground {
	companion object {
		const val BACKGROUND = 0xFF000000.toInt()
	}

	object Screen {
		const val PADDING_TOP = 0f // virtual px
		const val VERTICAL_SEPARATOR_WIDTH = 20f // virtual px
		const val WIDTH = 480f // virtual px, used to normalize the real viewport width to this
	}

	object Playground {
		const val COLOR_GRID = 0xFF404040.toInt()

		// Total width = 10 for columns + 4 for preview + 2 for preview padding = 16 blocks wide. There
		// are two vertical separators on both sides, and one between the playground and the preview.
		const val CELL_SIZE = (Screen.WIDTH - 2 * Screen.VERTICAL_SEPARATOR_WIDTH) / 16 // virtual px
		const val COLUMNS = 10
		const val ROWS = 20
	}

	object Preview {
		const val COLUMNS = 6
		const val ROWS = 6
	}

	object Wall {
		const val COLOR_BRICK = 0xFF9A4F3A.toInt()
		const val COLOR_BRICK_DARK = 0xFF67352C.toInt()
		const val COLOR_BRICK_LIGHT = 0xFFC06A4F.toInt()
		const val COLOR_MORTAR = 0xFF3A2521.toInt()

		const val BRICK_HEIGHT = 12f // virtual px
		const val BRICK_GAP = 2f // virtual px
		const val EDGE_GAP = 2f // virtual px
	}

	private var drawCommands: List<DrawCommand> = emptyList()

	private var cellSize = 0f

	private var playgroundTop = 0f
	private var playgroundLeft = 0f
	private var playgroundWidth = 0f
	private var playgroundHeight = 0f

	private var previewLeft = 0f
	private var previewWidth = 0f
	private var previewHeight = 0f
	private var wallWidth = 0f

	private var scoreboardTop = 0f
	private var scoreboardLeft = 0f


	fun cellSize() = cellSize
	fun draw() = DrawCommandGroup(0f, 0f, 0f, drawCommands)
	fun dimensions() = Pair(Playground.COLUMNS, Playground.ROWS)
	fun position() = Pair(playgroundLeft, playgroundTop)
	fun scoreboardPosition() = Pair(scoreboardLeft, scoreboardTop)
	fun scoreboardWidth() = previewWidth


	fun create(viewportWidth: Float) {
		val scale = viewportWidth / Screen.WIDTH

		cellSize = Playground.CELL_SIZE * scale

		playgroundTop = Screen.PADDING_TOP * scale
		playgroundLeft = Screen.VERTICAL_SEPARATOR_WIDTH * scale
		playgroundWidth = Playground.COLUMNS * cellSize
		playgroundHeight = Playground.ROWS * cellSize

		previewLeft = playgroundLeft + playgroundWidth + Screen.VERTICAL_SEPARATOR_WIDTH * scale
		previewWidth = Preview.COLUMNS * cellSize
		previewHeight = Preview.ROWS * cellSize

		wallWidth = Screen.VERTICAL_SEPARATOR_WIDTH * scale

		scoreboardTop = playgroundTop + wallWidth + previewHeight + wallWidth
		scoreboardLeft = previewLeft

		drawCommands =  drawGrid(scale) + drawPlayground(scale) + drawPreviewBox(scale)
	}


	private fun drawGrid(scale: Float): List<DrawCommand> {
		val commands = mutableListOf<DrawCommand>()

		for (row in 0..Playground.ROWS) {
			val y = playgroundTop + row * Playground.CELL_SIZE * scale
			commands.add(DrawCommand.Line(playgroundLeft, y, playgroundLeft + playgroundWidth, y, Playground.COLOR_GRID))
		}

		for (col in 0..Playground.COLUMNS) {
			val x = playgroundLeft + col * Playground.CELL_SIZE * scale
			commands.add(DrawCommand.Line(x, playgroundTop, x, playgroundTop + playgroundHeight, Playground.COLOR_GRID))
		}

		return commands
	}


	private fun drawPlayground(scale: Float): List<DrawCommand> {
		return drawBrickWall( // left wall
				left = 0f,
				top = playgroundTop,
				right = playgroundLeft,
				bottom = playgroundHeight + wallWidth,
				scale = scale
			) +

			drawBrickWall( // central wall
				left = playgroundLeft + playgroundWidth,
				top = playgroundTop,
				right = playgroundLeft + playgroundWidth + wallWidth,
				bottom = playgroundHeight + wallWidth,
				scale = scale
			) +

			drawBrickWall( // bottom wall
				left = 0f,
				top = playgroundTop + playgroundHeight,
				right = Screen.WIDTH * scale,
				bottom = playgroundTop + playgroundHeight + wallWidth,
				scale = scale,
				horizontal = true
			)
	}


	private fun drawPreviewBox(scale: Float): List<DrawCommand> {
		return drawBrickWall( // top wall
			left = previewLeft,
			top = playgroundTop,
			right = Screen.WIDTH * scale,
			bottom = playgroundTop + wallWidth,
			scale = scale,
			horizontal = true
		) + drawBrickWall( // bottom wall
			left = previewLeft,
			top = playgroundTop + wallWidth + previewHeight,
			right = Screen.WIDTH * scale,
			bottom = playgroundTop + wallWidth + previewHeight + wallWidth,
			scale = scale,
			horizontal = true
		)
	}


	private fun drawBrickWall(
		left: Float,
		top: Float,
		right: Float,
		bottom: Float,
		scale: Float,
		horizontal: Boolean = false
	): List<DrawCommand> {
		val commands = mutableListOf<DrawCommand>()

		// dark base behind the bricks
		commands += DrawCommand.Rect(left, top, right, bottom, 0f, Wall.COLOR_BRICK_DARK, true)

		val brickHeight = Wall.BRICK_HEIGHT * scale
		val gap = Wall.BRICK_GAP * scale
		val edgeGap = Wall.EDGE_GAP * scale

		commands += if (horizontal) {
			drawHorizontalBricks(left, top, right, bottom, brickHeight, gap, edgeGap)
		} else {
			drawVerticalBricks(left, top, right, bottom, brickHeight, gap, edgeGap)
		}

		return commands
	}


	private fun drawHorizontalBricks(
		left: Float,
		top: Float,
		right: Float,
		bottom: Float,
		brickHeight: Float,
		gap: Float,
		edgeGap: Float
	): List<DrawCommand> {
		val commands = mutableListOf<DrawCommand>()

		var column = 0
		var x = left + edgeGap

		while (x < right - edgeGap) {
			val brickRight = minOf(x + brickHeight - gap, right - edgeGap)

			// alternate columns to create staggered masonry effect
			val inset = if (column % 2 == 0) 0f else gap * 0.75f

			commands += DrawCommand.Rect(
				left = x,
				top = top + edgeGap + inset,
				right = brickRight,
				bottom = bottom - edgeGap,
				rotateDeg = 0f,
				color = when (column % 4) {
					0 -> Wall.COLOR_BRICK_LIGHT
					1 -> Wall.COLOR_BRICK
					2 -> Wall.COLOR_BRICK
					else -> Wall.COLOR_BRICK_DARK
				},
				filled = true
			)

			// Mortar line between bricks.
			if (brickRight < right - edgeGap) {
				commands += DrawCommand.Line(
					x1 = brickRight + gap * 0.5f,
					y1 = top + edgeGap,
					x2 = brickRight + gap * 0.5f,
					y2 = bottom - edgeGap,
					color = Wall.COLOR_MORTAR
				)
			}

			column++
			x += brickHeight
		}

		return commands
	}

	private fun drawVerticalBricks(
		left: Float,
		top: Float,
		right: Float,
		bottom: Float,
		brickHeight: Float,
		gap: Float,
		edgeGap: Float
	): List<DrawCommand> {
		val commands = mutableListOf<DrawCommand>()
		var row = 0
		var y = top + edgeGap

		while (y < bottom - edgeGap) {
			val brickBottom = minOf(y + brickHeight - gap, bottom - edgeGap)

			// alternate rows slightly to create a masonry pattern
			val inset = if (row % 2 == 0) 0f else gap * 0.75f

			commands += DrawCommand.Rect(
				left = left + edgeGap + inset,
				top = y,
				right = right - edgeGap,
				bottom = brickBottom,
				rotateDeg = 0f,
				color = when (row % 4) {
					0 -> Wall.COLOR_BRICK_LIGHT
					1 -> Wall.COLOR_BRICK
					2 -> Wall.COLOR_BRICK
					else -> Wall.COLOR_BRICK_DARK
				},
				filled = true
			)

			// mortar line across the wall
			if (brickBottom < bottom - edgeGap) {
				commands += DrawCommand.Line(
					x1 = left + edgeGap,
					y1 = brickBottom + gap * 0.5f,
					x2 = right - edgeGap,
					y2 = brickBottom + gap * 0.5f,
					color = Wall.COLOR_MORTAR
				)
			}

			row++
			y += brickHeight
		}

		return commands
	}
}
