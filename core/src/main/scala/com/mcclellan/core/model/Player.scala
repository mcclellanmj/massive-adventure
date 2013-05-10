package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.World


class Player(_position : Vector2[Float], _rotation : Float, protected val world : World) 
	extends DynamicBody with CircleFixture {
	this.position = _position
	this.rotation = _rotation
	this.body.setLinearDamping(3.0f)
	lazy val size = 10.0f * .01f
	
	override def toString = this.position.toString
}