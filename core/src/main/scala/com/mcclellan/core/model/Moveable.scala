package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2

trait Positionable {
	def position : Vector2[Float]
	def rotation : Float
}

trait Moveable extends Positionable {
	def velocity : Vector2[Float]
}

trait MaxSpeed extends Moveable {
	def maxVelocity : Float
}

trait Accelerating {
	def acceleration : Vector2[Float]
}

trait DerivedRotation {
	def velocity : Vector2[Float]
	def rotation = Math.toDegrees(velocity.angle).toFloat
}

trait Updateable {
	def update(elapsed : Float) : Unit
}

trait MoveableUpdate extends Updateable {
	def position : Vector2[Float]
	def position_=(position : Vector2[Float])
	def velocity : Vector2[Float]
	def update(elapsed : Float) = {
		position = position + (velocity * elapsed)
	}
}