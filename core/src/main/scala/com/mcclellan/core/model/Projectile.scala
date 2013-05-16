package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.math.{Vector2 => GdxVector}
import com.mcclellan.core.physics.WorldConnector
import com.badlogic.gdx.graphics.g2d.Sprite

class Projectile(val _position : Vector2, val _velocity : Vector2, val dmg : Integer)(implicit val world : WorldConnector)
	extends DynamicBody with BulletFixture with Drawable {
	lazy val size = .01f
	body.setBullet(true)
	this.position = _position
	this.velocity = _velocity
	rotation= _velocity.angle
}