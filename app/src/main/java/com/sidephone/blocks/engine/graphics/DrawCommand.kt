package com.sidephone.blocks.engine.graphics

sealed class DrawCommand {
	data class Arc(val cx: Float, val cy: Float, val radius: Float, val startAngle: Float, val sweepAngle: Float, val color: Int, val filled: Boolean) : DrawCommand()
	data class Circle(val cx: Float, val cy: Float, val radius: Float, val color: Int, val filled: Boolean) : DrawCommand()
	data class Dot(val x: Float, val y: Float, val color: Int) : DrawCommand()
	data class Line(val x1: Float, val y1: Float, val x2: Float, val y2: Float, val color: Int) : DrawCommand()
	data class Polygon(val points: List<Pair<Float, Float>>, val rotateDeg: Float, val color: Int, val filled: Boolean) : DrawCommand()
	data class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float, val rotateDeg: Float, val color: Int, val filled: Boolean) : DrawCommand()
	data class Text(val text: String, val x: Float, val y: Float, val textSize: Float, val color: Int) : DrawCommand()

	fun translatedRect(left: Float, top: Float, original: Rect): Rect {
		return Rect(
			left = original.left + left,
			top = original.top + top,
			right = original.right + left,
			bottom = original.bottom + top,
			rotateDeg = original.rotateDeg,
			color = original.color,
			filled = original.filled
		)
	}
}
