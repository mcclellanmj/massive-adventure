package com.mcclellan.core.math
import Numeric.Implicits._
import com.badlogic.gdx.math.MathUtils

case class Vector2(val x : Float, val y : Float) {
	def +(other : Vector2) = Vector2(x + other.x, y + other.y)
	def -(other : Vector2) = Vector2(x - other.x, y - other.y)
	def *(scale : Float) = Vector2(x * scale, y * scale)
	def *(vector : Vector2) = x*vector.x + y*vector.y
	def rotate(angle : Angle) = {
		val cos : Float = Math.cos(angle.radians).toFloat
		val sin : Float = Math.sin(angle.radians).toFloat

		new Vector2(x * cos - y * sin, x * sin + y * cos);
	}
	lazy val unary_- = Vector2(-this.x, -this.y)

	lazy val magnitude = Math.sqrt((x*x + y*y)).toFloat
	lazy val angle = Radians(Math.atan2(x, -y).toFloat)
	lazy val unit = {
		if(y == 0) Vector2(x, 0)
		else Vector2(x/magnitude, y/magnitude)
	}
	
	override def equals(other : Any) = other match {
		case vec:Vector2 => x == vec.x && y == vec.y
		case _ => false
	}
	
	override def toString = "(" + x + "," + y + ")"
}

object Vector2 {
	def fromAngle(angle : Angle) = {
		Vector2(Math.sin(-angle.radians).toFloat, Math.cos(angle.radians).toFloat)
	}
}