package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.math.{Vector2 => GdxVector}
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.World


class Player(_position : Vector2[Float], _rotation : Float, protected val world : World) 
	extends DynamicBody with CircleFixture {
	this.bodyDef.position.set(_position.x, _position.y)
	this.bodyDef.angle = _rotation
	val size = 10
	
	def rotation = this.body.getAngle
	def rotation_=(angle:Float) = this.body.setTransform(this.body.getPosition, angle)
	def position = new Vector2[Float](this.body.getPosition.x, this.body.getPosition.y)
	def position_=(pos:Vector2[Float]) = this.body.setTransform(new GdxVector(pos.x, pos.y), this.body.getAngle)
	def velocity = this.body.getLinearVelocity
	def velocity_=(vel:Vector2[Float]) = this.body.setLinearVelocity(vel.x, vel.y)
}