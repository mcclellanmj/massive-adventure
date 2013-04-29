package com.mcclellan.core.model

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.CircleShape
import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.math.{Vector2 => GdxVector}
import com.badlogic.gdx.physics.box2d.Filter
import com.badlogic.gdx.physics.box2d.MassData

abstract class DynamicBody {
	protected def world : World
	val bodyDef = new BodyDef
	bodyDef.`type` = BodyType.DynamicBody
	val body = world.createBody(bodyDef)
	body.setUserData(this)
	
	def rotation = this.body.getAngle
	def rotation_=(angle:Float) = this.body.setTransform(this.body.getPosition, angle)
	def position = new Vector2[Float](this.body.getPosition.x, this.body.getPosition.y)
	def position_=(pos:Vector2[Float]) = this.body.setTransform(new GdxVector(pos.x, pos.y), this.body.getAngle)
	def velocity : Vector2[Float] = new Vector2(this.body.getLinearVelocity.x, this.body.getLinearVelocity.y)
	def velocity_=(vel:Vector2[Float]) = this.body.setLinearVelocity(vel.x, vel.y)
}

trait CircleFixture {
	def body : Body
	def size : Float
	
	val fixture = new FixtureDef
	val circle = new CircleShape
	circle.setRadius(size)
	fixture.shape = circle
	fixture.density = 1
	body.createFixture(fixture)
}

trait BulletFixture {
	def body : Body
	def size : Float
	
	val fixture = new FixtureDef
	val circle = new CircleShape
	circle.setRadius(size)
	fixture.shape = circle
	fixture.filter.categoryBits = 0x0100
	fixture.filter.maskBits = 0x0001
	fixture.density = .01f
	fixture.restitution = .3f
	body.createFixture(fixture)
}