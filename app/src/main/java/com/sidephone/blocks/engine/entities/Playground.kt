package com.sidephone.blocks.engine.entities

import com.sidephone.blocks.engine.graphics.DrawCommand
import com.sidephone.blocks.engine.graphics.DrawCommandGroup


class Playground {
	object Color {
		const val BACKGROUND = 0xFF000000.toInt()
		const val GRID_BAR = 0xFF444444.toInt()
		const val GRID_BORDER = 0xFFFFFFFF.toInt()

		const val WALL = 0xFF9A4F3A.toInt()        // main terracotta brick
		const val WALL_LIGHT = 0xFFC06A4F.toInt()  // sunlit brick
		const val WALL_DARK = 0xFF67352C.toInt()   // dark mortar/base
		const val WALL_MORTAR = 0xFF3A2521.toInt() // deep brown mortar
	}

	object Screen {
		const val PADDING_TOP = 120f // virtual px
		const val VERTICAL_SEPARATOR_WIDTH = 20f // virtual px
		const val WIDTH = 480f // virtual px, used to normalize the real viewport width to this
	}

	object Playground {
		// Total width = 10 for columns + 4 for preview + 2 for preview padding = 16 blocks wide. There
		// are two vertical separators on both sides, and one between the playground and the preview.
		const val CELL_WIDTH = (Screen.WIDTH - 3 * Screen.VERTICAL_SEPARATOR_WIDTH) / 16 // virtual px
		const val CELL_HEIGHT = CELL_WIDTH * 0.95f // virtual px
		const val COLUMNS = 10
		const val ROWS = 24
		const val ROWS_VISIBLE = 20
	}

	object Preview {
		const val CELL_WIDTH = Playground.CELL_WIDTH
		const val CELL_HEIGHT = Playground.CELL_HEIGHT
		const val COLUMNS = 6
		const val ROWS = 6
	}

	object Wall {
		const val BRICK_HEIGHT = 12f // virtual px
		const val BRICK_GAP = 2f // virtual px
		const val EDGE_GAP = 2f // virtual px
	}

	private var drawCommands: List<DrawCommand> = emptyList()

	private var top = 0f
	private var left = 0f
	private var gridWidth = 0f
	private var gridHeight = 0f

	private var previewLeft = 0f
	private var previewWidth = 0f
	private var previewHeight = 0f


	fun draw() = DrawCommandGroup(0f, 0f, 0f, drawCommands)


	fun create(viewportWidth: Float) {
		val scale = viewportWidth / Screen.WIDTH

		top = (Screen.PADDING_TOP * scale)
		left = (Screen.VERTICAL_SEPARATOR_WIDTH * scale)
		gridWidth = Playground.COLUMNS * Playground.CELL_WIDTH * scale
		gridHeight = Playground.ROWS_VISIBLE * Playground.CELL_HEIGHT * scale

		previewLeft = left + gridWidth + Screen.VERTICAL_SEPARATOR_WIDTH * scale
		previewWidth = Preview.COLUMNS * Preview.CELL_WIDTH * scale
		previewHeight = Preview.ROWS * Preview.CELL_HEIGHT * scale

		drawCommands =  drawDebugGrid(scale) + drawSeparatorWalls(scale) + drawPlayground() + drawPreviewBox()
	}



	private fun drawPlayground(): List<DrawCommand> {
		return listOf(
			DrawCommand.Rect(left, top, left + gridWidth, top + gridHeight, 0f, Color.GRID_BORDER, false)
		)
	}


	private fun drawDebugGrid(scale: Float): List<DrawCommand> {
		val commands = mutableListOf<DrawCommand>()

		for (row in 0..Playground.ROWS_VISIBLE) {
			val y = top + row * Playground.CELL_HEIGHT * scale
			commands.add(DrawCommand.Line(left, y, left + gridWidth, y, Color.GRID_BAR))
		}

		for (col in 0..Playground.COLUMNS) {
			val x = left + col * Playground.CELL_WIDTH * scale
			commands.add(DrawCommand.Line(x, top, x, top + gridHeight, Color.GRID_BAR))
		}

		return commands
	}


	private fun drawPreviewBox(): List<DrawCommand> {
		return listOf(
			DrawCommand.Rect(previewLeft, top, previewLeft + previewWidth, top + previewHeight, 0f, Color.GRID_BORDER, false)
		)
	}


	private fun drawSeparatorWalls(scale: Float): List<DrawCommand> {
		val commands = mutableListOf<DrawCommand>()

		val separatorWidth = Screen.VERTICAL_SEPARATOR_WIDTH * scale

		// Left wall
		val leftWallX = left - separatorWidth

		commands += drawBrickWall(
			left = leftWallX,
			top = top,
			right = left,
			bottom = top + gridHeight,
			scale = scale
		)

		// Central wall
		val centralWallX = left + gridWidth

		commands += drawBrickWall(
			left = centralWallX,
			top = top,
			right = centralWallX + separatorWidth,
			bottom = top + gridHeight,
			scale = scale
		)

		// Top wall
		val topWallY = top - separatorWidth

		commands += drawBrickWall(
			left = leftWallX,
			top = topWallY,
			right = Screen.WIDTH * scale,
			bottom = top,
			scale = scale,
			horizontal = true
		)

		return commands
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

		// Dark base behind the bricks.
		commands += DrawCommand.Rect(
			left = left,
			top = top,
			right = right,
			bottom = bottom,
			rotateDeg = 0f,
			color = Color.WALL_DARK,
			filled = true
		)

		val brickHeight = Wall.BRICK_HEIGHT * scale
		val gap = Wall.BRICK_GAP * scale
		val edgeGap = Wall.EDGE_GAP * scale

		if (horizontal) {
			drawHorizontalBricks(
				commands,
				left,
				top,
				right,
				bottom,
				brickHeight,
				gap,
				edgeGap
			)
		} else {
			drawVerticalBricks(
				commands,
				left,
				top,
				right,
				bottom,
				brickHeight,
				gap,
				edgeGap
			)
		}

		return commands
	}


	private fun drawHorizontalBricks(
		commands: MutableList<DrawCommand>,
		left: Float,
		top: Float,
		right: Float,
		bottom: Float,
		brickHeight: Float,
		gap: Float,
		edgeGap: Float
	) {
		var column = 0
		var x = left + edgeGap

		while (x < right - edgeGap) {
			val brickRight = minOf(x + brickHeight - gap, right - edgeGap)

			// Alternate columns to create the same staggered masonry effect.
			val inset = if (column % 2 == 0) 0f else gap * 0.75f

			commands += DrawCommand.Rect(
				left = x,
				top = top + edgeGap + inset,
				right = brickRight,
				bottom = bottom - edgeGap,
				rotateDeg = 0f,
				color = when (column % 4) {
					0 -> Color.WALL_LIGHT
					1 -> Color.WALL
					2 -> Color.WALL
					else -> Color.WALL_DARK
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
					color = Color.WALL_MORTAR
				)
			}

			column++
			x += brickHeight
		}
	}

	private fun drawVerticalBricks(
		commands: MutableList<DrawCommand>,
		left: Float,
		top: Float,
		right: Float,
		bottom: Float,
		brickHeight: Float,
		gap: Float,
		edgeGap: Float
	) {
		var row = 0
		var y = top + edgeGap

		while (y < bottom - edgeGap) {
			val brickBottom = minOf(y + brickHeight - gap, bottom - edgeGap)

			// Alternate rows slightly to create a masonry pattern.
			val inset = if (row % 2 == 0) 0f else gap * 0.75f

			commands += DrawCommand.Rect(
				left = left + edgeGap + inset,
				top = y,
				right = right - edgeGap,
				bottom = brickBottom,
				rotateDeg = 0f,
				color = when (row % 4) {
					0 -> Color.WALL_LIGHT
					1 -> Color.WALL
					2 -> Color.WALL
					else -> Color.WALL_DARK
				},
				filled = true
			)

			// Mortar line across the wall.
			if (brickBottom < bottom - edgeGap) {
				commands += DrawCommand.Line(
					x1 = left + edgeGap,
					y1 = brickBottom + gap * 0.5f,
					x2 = right - edgeGap,
					y2 = brickBottom + gap * 0.5f,
					color = Color.WALL_MORTAR
				)
			}

			row++
			y += brickHeight
		}
	}
}
