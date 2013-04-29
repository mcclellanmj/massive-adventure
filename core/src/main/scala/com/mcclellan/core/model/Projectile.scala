package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.math.{Vector2 => GdxVector}

class Projectile(val _position : Vector2[Float], val _velocity : Vector2[Float], protected val world : World)
	extends DynamicBody with BulletFixture {
	lazy val size = .01f
	body.setBullet(true)
	this.position = _position
	this.velocity = _velocity
	rotation=Math.toDegrees(_velocity.angle.toFloat).toFloat
}