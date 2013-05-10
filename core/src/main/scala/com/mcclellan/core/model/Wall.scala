package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.mcclellan.core.implicits.VectorImplicits._

class Wall(val start : Vector2[Float], val end : Vector2[Float])(implicit val world : World) {
	val wallDef = new BodyDef
	wallDef.`type` = BodyDef.BodyType.StaticBody
	val wall = world.createBody(wallDef)

	val fixtureDef = new FixtureDef
	val shape = new EdgeShape
	shape.set(start, end)
	fixtureDef.shape = shape
	fixtureDef.density = 100
	wall.createFixture(fixtureDef)
}