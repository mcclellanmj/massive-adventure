package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2
import com.mcclellan.core.math.Angle
import com.mcclellan.core.physics.WorldConnector
import com.mcclellan.core.GameContainer
import com.mcclellan.core.math.Degrees
import com.mcclellan.core.implicits.VectorImplicits.toGdxVector
import com.badlogic.gdx.graphics.g2d.Sprite

trait Humanoid {

}

class Player(_position : Vector2, _rotation : Angle)(implicit val world : WorldConnector)
	extends DynamicBody with CircleFixture with Updateable with Drawable {
	this.position = _position
	this.rotation = _rotation
	this.body.setLinearDamping(3.0f)
	lazy val size = 10.0f * .01f
	var primary : Option[Weapon] = None
	var secondary : Option[Weapon] = None
	var firing : Option[Weapon] = None
	var health = 100
	
	def arm(toEquip : Option[Weapon]) = firing = toEquip
	def disarm = firing = None
	
	override def update(elapsed : Float) = firing.map(_.update(elapsed))
	
	override def toString = this.position.toString
}

class Enemy(_position : Vector2)(implicit val world : WorldConnector, game : GameContainer) 
		extends DynamicBody with CircleFixture with Updateable with Drawable {
	this.position = _position
	var _health = 10
	this.body.setLinearDamping(3.0f)
	this.body.setFixedRotation(true)
	lazy val size = 10.0f * .01f
	def health = _health
	def health_=(newHealth:Int) = {
		_health = newHealth
		if(_health < 1) {
			game.removeComponent(this)
		}
	}
	override def rotation = velocity.angle

	def update(elapsed : Float) {
		if((game.player.position - position).magnitude > .5) {
			body.applyForceToCenter((game.player.position - position).unit * .1f, true)
			body.setLinearVelocity(body.getLinearVelocity().limit(.6f))
		}
	}

	override def toString = this.position.toString
}