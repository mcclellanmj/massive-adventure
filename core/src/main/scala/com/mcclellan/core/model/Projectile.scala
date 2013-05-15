package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.math.{Vector2 => GdxVector}
import com.mcclellan.core.physics.WorldConnector

class Projectile(val _position : Vector2, val _velocity : Vector2)(implicit val world : WorldConnector)
	extends DynamicBody with BulletFixture {
	lazy val size = .01f
	body.setBullet(true)
	this.position = _position
	this.velocity = _velocity
	rotation= _velocity.angle
}