package com.mcclellan.core.physics

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.Body
import com.mcclellan.core.model.Player

class WorldConnectorImpl(private val world : World) extends WorldConnector {
	def createBody(body : BodyDef) = world.createBody(body)
	def deleteBody(body : Body) = world.destroyBody(body)
	def step(elapsed : Float) = world.step(elapsed, 4, 1)
}

trait WorldConnector {
	def createBody(body : BodyDef) : Body
	def deleteBody(body : Body)
	def step(elapsed : Float)
}