package com.mcclellan.core.model

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.CircleShape

abstract class DynamicBody {
	protected def world : World
	val bodyDef = new BodyDef
	bodyDef.`type` = BodyType.DynamicBody
	val body = world.createBody(bodyDef)
}

trait CircleFixture {
	def body : Body
	def size : Int
	
	val fixture = new FixtureDef
	val circle = new CircleShape
	circle.setRadius(size)
	fixture.shape = circle
	fixture.density = 1
	body.createFixture(fixture)
}