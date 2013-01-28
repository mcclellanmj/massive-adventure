package com.mcclellan.models
import Numeric.Implicits._
import scala.math.sqrt

class Vector2d (
	val x : Double,
	val y : Double) {

	def +(other : Vector2d) = new Vector2d(x + other.x, y + other.y)
	def -(other : Vector2d) = new Vector2d(x - other.x, y - other.y)
	def *(scale : Double) = new Vector2d(x * scale, y * scale)
	def dot(other : Vector2d) = x * other.x + y * other.y
	def cross(other : Vector2d) =  x * other.y - y * other.x
	def perpendicular() = new Vector2d(-y, x)
	lazy val magnitude = sqrt(x*x + y*y)
	def unit() = magnitude match {
	  case 0 => Vector2d.zero
	  case magnitude:Double => new Vector2d(x/magnitude, y/magnitude)
	}
	
	def unary_-() = new Vector2d(-x, -y)

	override def equals(that: Any) = {
		that match {
			case other : Vector2d => (other.x == x) && (other.y == y)
			case _ => false
		}
	}
	
	override lazy val toString = "<%s, %s>" format(x, y)
}

object Vector2d {
  lazy val unitX = new Vector2d(1, 0)
  lazy val unitY = new Vector2d(0, 1)
  lazy val zero = new Vector2d(0, 0);
}