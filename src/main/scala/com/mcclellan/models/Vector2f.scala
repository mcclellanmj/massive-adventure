package com.mcclellan.models
import Numeric.Implicits._
import scala.math.sqrt

case class Vector2f (
	val x : Float,
	val y : Float) {

	def +(other : Vector2f) = Vector2f(x + other.x, y + other.y)
	def -(other : Vector2f) = Vector2f(x - other.x, y - other.y)
	def *(scale : Float) = Vector2f(x * scale, y * scale)
	def dot(other : Vector2f) = x * other.x + y * other.y
	def cross(other : Vector2f) =  x * other.y - y * other.x
	def perpendicular() = Vector2f(-y, x)
	lazy val magnitude = sqrt(x*x + y*y).toFloat
	def unit() = magnitude match {
	  case 0 => Vector2f.zero
	  case magnitude:Float => Vector2f(x/magnitude, y/magnitude)
	}
	
	def unary_-() = new Vector2f(-x, -y)

	override def equals(that: Any) = {
		that match {
			case Vector2f(othX, othY) => (othX == x) && (othY == y)
			case _ => false
		}
	}
	
	override lazy val toString = "<%s, %s>" format(x, y)
}

object Vector2f {
  lazy val unitX = new Vector2f(1, 0)
  lazy val unitY = new Vector2f(0, 1)
  lazy val zero = new Vector2f(0, 0);
}