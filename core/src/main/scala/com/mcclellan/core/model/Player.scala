package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.mcclellan.core.physics.WorldConnector
import com.mcclellan.core.math.Angle


class Player(_position : Vector2, _rotation : Angle)(implicit val world : WorldConnector) 
	extends DynamicBody with CircleFixture {
	this.position = _position
	this.rotation = _rotation
	this.body.setLinearDamping(3.0f)
	lazy val size = 10.0f * .01f
	var health = 100
	
	override def toString = this.position.toString
}