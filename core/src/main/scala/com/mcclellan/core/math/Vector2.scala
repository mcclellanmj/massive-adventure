package com.mcclellan.core.math
import Numeric.Implicits._

class Vector2[T : Numeric](val x : T, val y : T) {
	def +(other : Vector2[T]) = new Vector2[T](x + other.x, y + other.y)
	def -(other : Vector2[T]) = new Vector2[T](x - other.x, y - other.y)
	def unary_- = new Vector2[T](-this.x, -this.y)

	def *(scale : T) = new Vector2[T](x * scale, y * scale)
	def magnitude = Math.sqrt((x*x + y*y).toDouble)
	def angle = Math.atan(y.toDouble/x.toDouble)
}