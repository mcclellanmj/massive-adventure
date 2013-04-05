package com.mcclellan.core.math
import Numeric.Implicits._

class Vector2[T : Numeric](val x : T, val y : T) {
	def +(other : Vector2[T]) = new Vector2[T](x + other.x, y + other.y)
	def -(other : Vector2[T]) = new Vector2[T](x - other.x, y - other.y)
	def *(scale : T) = new Vector2[T](x * scale, y * scale)
	def *(vector : Vector2[T]) = x*vector.x + y*vector.y
	lazy val unary_- = new Vector2[T](-this.x, -this.y)

	lazy val magnitude = Math.sqrt((x*x + y*y).toDouble)
	lazy val angle = Math.atan(y.toDouble/x.toDouble)
	lazy val unit = new Vector2[Double](x.toDouble/magnitude, y.toDouble/magnitude)
	
	lazy val toDouble = new Vector2[Double](x.toDouble, y.toDouble)
	lazy val toFloat = new Vector2[Float](x.toFloat, y.toFloat)
	lazy val toInt = new Vector2[Int](x.toInt, y.toInt)
	lazy val toLong = new Vector2[Long](x.toLong, y.toLong)
	
	override def equals(other : Any) = other match {
		case vec:Vector2[_] => x == vec.x && y == vec.y
		case _ => false
	}
}
