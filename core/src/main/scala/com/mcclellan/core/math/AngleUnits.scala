package com.mcclellan.core.math

object Conversions {
	private val _radiansToDegrees = 57.2957795f
	private val _degreesToRadians = 0.0174532925f
	
	def radiansToDegrees(value : Float) = value * _radiansToDegrees
	def degreesToRadians(value : Float) = value * _degreesToRadians
}

trait Angle {
	def degrees : Float
	def radians : Float
}

case class Radians(value:Float) extends Angle {
	lazy val degrees = Conversions.radiansToDegrees(value)
	val radians = value 
}

case class Degrees(val value:Float) extends Angle {
	val degrees = value
	lazy val radians = Conversions.degreesToRadians(value)
}