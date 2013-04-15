package com.mcclellan.core.math
import Numeric.Implicits._

class Vector2[T : Numeric](val x : T, val y : T) {
	def +(other : Vector2[T]) = new Vector2[T](x + other.x, y + other.y)
	def -(other : Vector2[T]) = new Vector2[T](x - other.x, y - other.y)
	def *(scale : T) = new Vector2[T](x * scale, y * scale)
	def *(vector : Vector2[T]) = x*vector.x + y*vector.y
	lazy val unary_- = new Vector2[T](-this.x, -this.y)

	lazy val magnitude = Math.sqrt((x*x + y*y).toDouble).toFloat
	lazy val angle = Math.atan2(x.toDouble, y.toDouble)
	lazy val unit = {
		if(y == 0) new Vector2[Float](x.toFloat, 0)
		else new Vector2[Float](x.toFloat/magnitude.toFloat, y.toFloat/magnitude.toFloat)
	}
	
	lazy val toDouble = new Vector2[Double](x.toDouble, y.toDouble)
	lazy val toFloat = new Vector2[Float](x.toFloat, y.toFloat)
	lazy val toInt = new Vector2[Int](x.toInt, y.toInt)
	lazy val toLong = new Vector2[Long](x.toLong, y.toLong)
	
	override def equals(other : Any) = other match {
		case vec:Vector2[_] => x == vec.x && y == vec.y
		case _ => false
	}
	
	override def toString = "(" + x + "," + y + ")"
}