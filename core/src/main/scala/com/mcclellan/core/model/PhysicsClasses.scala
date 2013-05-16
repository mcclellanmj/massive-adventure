package com.mcclellan.core.model

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.CircleShape
import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.math.{ Vector2 => GdxVector }
import com.badlogic.gdx.physics.box2d.Filter
import com.badlogic.gdx.physics.box2d.MassData
import com.mcclellan.core.implicits.VectorImplicits._
import com.mcclellan.core.physics.WorldConnector
import com.mcclellan.core.math.Angle
import com.mcclellan.core.math.Degrees

abstract class DynamicBody extends BodyCreationListener with GameComponent  {
	protected def world : WorldConnector
	val bodyDef = new BodyDef
	bodyDef.`type` = BodyType.DynamicBody

	beforeBodyCreation(bodyDef)
	val body = world.createBody(bodyDef)
	afterBodyCreation(body)

	body.setUserData(this)

	def rotation : Angle = Degrees(this.body.getAngle)
	def rotation_=(angle : Angle) = this.body.setTransform(this.body.getPosition, angle.degrees)
	def position : Vector2 = this.body.getPosition
	def position_=(pos : Vector2) = this.body.setTransform(pos, this.body.getAngle)
	def velocity : Vector2 = this.body.getLinearVelocity
	def velocity_=(vel : Vector2) = this.body.setLinearVelocity(vel)
}

trait BodyCreationListener {
	protected def beforeBodyCreation(bodyDef : BodyDef) : Any = Unit
	protected def afterBodyCreation(body : Body) : Any = Unit
}

trait TransformableFixture {
	protected def transformFixture(fixture : FixtureDef) : FixtureDef = fixture
}

trait CircleFixture extends BodyCreationListener with TransformableFixture {
	def size : Float

	override def afterBodyCreation(body : Body) = {
		val fixture = new FixtureDef
		val circle = new CircleShape
		circle.setRadius(size)
		fixture.shape = circle
		fixture.density = 1
		body.createFixture(transformFixture(fixture))
	}
}

trait BulletFixture extends CircleFixture {

	override def transformFixture(fixture : FixtureDef) = {
		fixture.filter.categoryBits = 0x0100
		fixture.filter.maskBits = 0x0001
		fixture.density = .5f
		fixture.restitution = 1f
		fixture
	}
}