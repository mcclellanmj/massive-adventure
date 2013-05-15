package com.mcclellan.core.math

private object Conversions {
	private val _radiansToDegrees = 57.2957795f
	private val _degreesToRadians = 0.0174532925f
	
	def radiansToDegrees(value : Float) = value * _radiansToDegrees
	def degreesToRadians(value : Float) = value * _degreesToRadians
}

sealed trait Angle {
	def degrees : Float
	def radians : Float
}

sealed case class Radians(value:Float) extends Angle {
	lazy val degrees = Conversions.radiansToDegrees(value)
	val radians = value 
}

sealed case class Degrees(value:Float) extends Angle {
	val degrees = value
	lazy val radians = Conversions.degreesToRadians(value)
}